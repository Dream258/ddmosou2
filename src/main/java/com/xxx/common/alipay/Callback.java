package com.xxx.common.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



@Controller
@RequestMapping("alipay")
public class Callback {

    @Autowired
    AliPayConfig aliPayConfig;

    @RequestMapping("pay1")
    public void pay1(HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession session)
            throws IOException, AlipayApiException {
        String out_trade_no = new String(httpRequest.getParameter("WIDout_trade_no"));
        //付款金额，必填
        String total_amount = new String(httpRequest.getParameter("WIDtotal_amount"));
        //订单名称，必填
        String subject = new String(httpRequest.getParameter("WIDsubject"));
        //商品描述，可空
        String body = new String(httpRequest.getParameter("WIDbody"));

        //httpRequest.getSession().setAttribute("subject", subject);
        //httpRequest.getSession().setAttribute("body", body);
        session.setAttribute("subject", subject);
        session.setAttribute("body", body);
        session.setAttribute("ceshi", "123zlj");
        String body1 =(String) session.getAttribute("body");

        //AlipayClient alipayClient1 = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE); //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", aliPayConfig.getAppid(), aliPayConfig.getMerchantPrivateKey(),aliPayConfig.getFormate(),aliPayConfig.getCharset(),aliPayConfig.getAlipayPublicKey(),aliPayConfig.getSignType()); //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl(aliPayConfig.getReturnUrl());
        alipayRequest.setNotifyUrl(aliPayConfig.getNotifyUrl());//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\"," + "\"subject\":\""+ subject +"\"," + "\"body\":\""+ body +"\"," + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        httpResponse.setContentType("text/html;charset=" + aliPayConfig.getCharset());
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();

    }

    @ResponseBody
    @RequestMapping("paymentOrder")
    public void paymentOrder(String money, String month, HttpServletResponse httpResponse, HttpServletRequest request)
            throws IOException, AlipayApiException {

        System.out.println("paymentOrder进来了");

        /**
         * 实例化客户端
         * 	参数：网关地址、商户APP_ID、商户私钥、格式、编码、支付宝公钥、加密类型
         */
        AlipayClient alipayClient = new DefaultAlipayClient(aliPayConfig.getGatewayUrl(), aliPayConfig.getAppid(), aliPayConfig.getMerchantPrivateKey(), aliPayConfig.getFormate(),
                aliPayConfig.getCharset(), aliPayConfig.getAlipayPublicKey(), aliPayConfig.getSignType());

        String outTradeNo = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
                + System.currentTimeMillis() + "";// 生成商户订单号
        AlipayTradePagePayModel payment = new AlipayTradePagePayModel();
        payment.setOutTradeNo(outTradeNo);// 订单号
        payment.setProductCode("FAST_INSTANT_TRADE_PAY");// 销售产品码（不可更改）
        payment.setTotalAmount(money);// 金额
        payment.setSubject("充值");// 标题
        payment.setBody("充值");// 描述
        payment.setTimeoutExpress("30m");// 该笔订单允许的最晚付款时间（30分钟）
        payment.setPassbackParams(month);

        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();// 创建API对应的request
        alipayRequest.setNotifyUrl(aliPayConfig.getNotifyUrl());// 在公共参数中设置回跳和通知地址
        alipayRequest.setBizModel(payment);// 分装参数
        String form = alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单

        httpResponse.setContentType("text/html;charset=" + aliPayConfig.getCharset());
        httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();// 关闭

    }


    @ResponseBody
    @RequestMapping("alipayNotify")
    public String alipayNotify(HttpServletRequest request) throws AlipayApiException {
        Map<String, String> params = new HashMap<String, String>();
        // 从支付宝回调的request域中取值
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        String tradeStatus = request.getParameter("trade_status"); // 交易状态
        boolean signVerified = AlipaySignature.rsaCheckV1(params, aliPayConfig.getAlipayPublicKey(), aliPayConfig.getCharset(), aliPayConfig.getSignType());// 调用SDK验证签名
        // 4.对验签进行处理
        if (signVerified) { // 验签通过
            if (tradeStatus.equals("TRADE_SUCCESS")) { // 只处理支付成功的订单: 修改交易表状态,支付成功
				/*BigDecimal total_amount = BigDecimal.valueOf(Double.valueOf(params.get("total_amount").toString()));// 充值金额
				User user = (User) request.getSession().getAttribute("user_login");
				String userTelephone = user.getUser_telephone();// 当前用户手机号
				String passback_params = params.get("passback_params").toString();
				chargerecordService.insertChargerecord(total_amount, userTelephone, passback_params);// 充值记录添加
				BigDecimal sumUserMoney = userService.sumUserMoney(userTelephone);// 使用手机号统计金额
				System.out.println(sumUserMoney);
				BigDecimal totalSum = sumUserMoney.add(total_amount);// 现用户总金额加充值金额
				userService.updateUserMoney(totalSum, userTelephone);// 使用手机号修改金额*/
                return "success";
            } else {
                return "fail";
            }
        } else { // 验签不通过
            return "fail";
        }
    }

    @RequestMapping("alipayReturn")
    public String alipayReturn(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, IOException {
        System.out.println("支付宝返回信息了");
        //获取支付宝GET过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator();iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i=0;i<values.length;i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
            //out.println(name+valueStr);
        }
        if(params!=null){
            //boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(params, aliPayConfig.getAlipayPublicKey(), aliPayConfig.getCharset(), aliPayConfig.getSignType()); //调用SDK验证签名

            //——请在这里编写您的程序（以下代码仅作参考）——
            if(signVerified) {
                //商户订单号
                String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

                //支付宝交易号
                String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

                //付款金额
                String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");

                //交易日期
                String timestamp = new String(request.getParameter("timestamp").getBytes("ISO-8859-1"),"UTF-8");

                //用户手机号
                /* User user = (User)session.getAttribute("user_login");
                String user_telephone = user.getUser_telephone(); */
                /* UserService userService = new UserServiceImpl(); */
                    /* User user = userService.selectUserByTelephone(user_telephone);
                String user_money1 = user.getUser_money();
                Double.valueOf(user_money1);
                int total_amount = Integer.valueOf(total_amount1);
                user_money=user_money+total_amount;
                userService.updateUserMoney(user_money,user_telephone);  */

                //out.println("支付宝交易号:"+trade_no+"<br/>商户订单号:"+out_trade_no+"<br/>付款金额:"+total_amount);
                //out.println("支付日期:"+timestamp);

                request.setAttribute("trade_no", trade_no);
                request.setAttribute("out_trade_no", out_trade_no);
                request.setAttribute("total_amount", total_amount);
                request.setAttribute("timestamp", timestamp);
                System.out.println("alipayReturn即将去odder");
                return "forward:/api/addOrder";
                //request.getRequestDispatcher("/orders/addOrder.do").forward(request, response);
            }else {
                System.out.println("alipayReturn即将出去,并失败");
                return "验签失败";
            }
        }else {
            System.out.println("alipayReturn即将出去,并失败");
            return "验签失败";
        }
    }

}
