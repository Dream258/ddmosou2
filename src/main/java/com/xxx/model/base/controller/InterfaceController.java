package com.xxx.model.base.controller;

import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.plugins.Page;
import com.xxx.common.interfaces.NotLogin;
import com.xxx.common.util.DateUtils;
import com.xxx.common.util.PDDUtils;
import com.xxx.model.base.entity.*;
import com.xxx.model.base.service.*;
import com.xxx.model.sys.utils.AliCloudSMS;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@NotLogin
@Controller
@RequestMapping("api")
public class InterfaceController {

    @Resource
    private DdMemberService ddMemberService;
    @Resource
    private DdOrdersService ddOrdersService;
    @Resource
    private DdViptimeService ddViptimeService;
    @Resource
    private DdRebatelogService ddRebatelogService;
    @Resource
    private DdrankingmonitorService ddrankingmonitorService;
    @Resource
    private DdKeyService ddKeyService;

    //首页
    @RequestMapping("/")
    public String page(){
        return "index";
    }
    //个人首页
    @RequestMapping("center")
    public String center(){
        return "member-center/index";
    }
    //购买排名监控页面
    @RequestMapping("ranking_monitor")
    public String ranking_monitor(){
        return "member-center/ranking_monitor";
    }
    //购买查询排名页面
    @RequestMapping("check_rank")
    public String check_rank(){
        return "member-center/check_rank";
    }
    //邀请页面
    @RequestMapping("share_list")
    public String share_list(){
        return "member-center/share-list";
    }
    //购买记录
    @RequestMapping("buylist")
    public String buylist(){ return "member-center/buy-list"; }
    //修改密码
    @RequestMapping("setpwd")
    public String setpwd(){ return "member-center/set"; }
    //排名监控
    @RequestMapping("ranking")
    public String ranking(){ return "ranking"; }

    //登录
    @ResponseBody
    @RequestMapping(value = { "/login_pc" }, method = {RequestMethod.POST} )
    public Map<String, Object> sysLogin(String mphone, String password, HttpSession session) {
        DdMember ddMember = ddMemberService.sysrLogin(mphone, password);
        Map<String, Object> result = new HashMap<String, Object>();
        Integer days = 0;//声明排名监控服务剩余天数
        if (ddMember != null) {
            List<DdOrders> lo = null;
            String telephone = ddMember.getUserTelephone();
            lo = ddOrdersService.getOrdersByTelephone(telephone);
            DdMember ddMember1 = null;
            if(lo==null) {
                if(ddMember.getUserType()==2) {
                    ddMemberService.updateUserType(telephone,1);
                    ddMember1 = ddMemberService.sysrLogin(mphone, password);
                }
                session.setAttribute("days", days);
            }else {
                if(ddMember.getUserType()==1) {
                    ddMemberService.updateUserType(telephone,2);
                    ddMember1 = ddMemberService.sysrLogin(mphone, password);
                }
                days = ddViptimeService.selectDays(telephone);
                session.setAttribute("days", days);
                session.setAttribute("listOrder", lo);
            }
            if(ddMember1!=null) {
                session.setAttribute("user_login", ddMember1);
            }else {
                session.setAttribute("user_login", ddMember);
            }
            result.put("result", 1);
        }
        return result;
    }

    /**
     * 退出登录
     */
    @ResponseBody
    @RequestMapping(value = { "/exituser" })
    public Integer exitSystem(HttpSession session) {
        session.removeAttribute("user_login");
        session.removeAttribute("days");
        return 1;
    }

    @ResponseBody
    @RequestMapping(value = { "/subordinate" })
    public int subordinate(HttpSession session, Page<DdMember> page, @RequestParam(value="pn",required=false,defaultValue="1") int pn) {
        page.setCurrent(pn);
        page.setSize(10);
        DdMember user = (DdMember)session.getAttribute("user_login");//获取登录用户信息
        Integer spnc = 0;//查询已邀请人充值的人数
        Integer spn = 0;//已邀请人数
        Integer sum = 0;
        List<Integer> count = null;//获得返利的金币和子级一一对应
        //List list = new ArrayList();
        if (user != null) {
            //查询已邀请人数
            spn = ddMemberService.selectPeopleNum(user.getId());
            Integer user_id = user.getId();
            Map map = new HashMap();
            map.put("user_id", user_id);
            //查询下级
            page = ddMemberService.selectSubordinate(page,map);
            //如果有下级再执行
            if(page.getSize()>0) {
                //查询已邀请人充值的人数
                spnc = ddMemberService.selectPeopleNumOrders(page.getRecords());
                count = new ArrayList<Integer>();
                for(DdMember u : page.getRecords()) {
                    String telephone = u.getUserTelephone();
                    // 根据被邀请人查询记录
                    Integer number = ddRebatelogService.selectByTelephoneS(telephone);
                    if(number!=null) {
                        sum = sum + number;
                        count.add(number);
                    }else {
                        count.add(0);
                    }
                }
            }
            session.setAttribute("subordinate", page.getRecords());
            session.setAttribute("count", count);
            session.setAttribute("spnc", spnc);
            session.setAttribute("spn", spn);
            session.setAttribute("sum", sum);
            session.setAttribute("subordinateTotal", page.getTotal());
            System.out.println("total="+page.getTotal());
        }
        return 1;
    }


    /**
     * 前台查询个人订单
     */
    @RequestMapping(value = { "/selectOrders" })
    @ResponseBody
    public int selectOrders(HttpSession session, Page<DdOrders> page, @RequestParam(value = "pn",defaultValue = "1") int pageNum) {
        page.setCurrent(pageNum);
        page.setSize(10);
        DdMember user = (DdMember) session.getAttribute("user_login");
        String telephone = user.getUserTelephone();
        Map map = new HashMap();
        map.put("telephone", telephone);
        page = ddOrdersService.getOrdersByTelephoneForPage(page,map);
        session.setAttribute("orderno", pageNum);
        session.setAttribute("listOrders", page.getRecords());
        session.setAttribute("orderpage", page);
        session.setAttribute("totalPage",page.getTotal());
        return 1;
    }

    //修改密码
    @ResponseBody
    @RequestMapping(value="toUpdate")
    public int toUpdate(String userTelephone,String new_password,String userPassword){
        if(ddMemberService.sysrLogin(userTelephone,userPassword)!=null){
            int i = ddMemberService.updateUserPassword(new_password,userTelephone);
            return i;
        }else{
            return 0;
        }
    }

    /**
     * 注册
     */
    @ResponseBody
    @RequestMapping(value= {"sysRegister"},method = { RequestMethod.POST })
    public int sysRegister(String user_telephone, String user_password, HttpSession session,
                           @RequestParam(value="user_code",defaultValue="123",required=false)String user_code) {
        DdMember user1 = ddMemberService.sysLoginTelephone(user_telephone);
        if(user1==null) {
            Integer user_id = null;
            if(!user_code.equals("123")) {
                // 根据邀请码查询ID
                user_id = ddMemberService.selectIDByCode(user_code);
            }
            //修改注册
            ddMemberService.addRegister(user_telephone, user_password,user_id);
            DdMember user2 = ddMemberService.sysrLogin(user_telephone, user_password);
            System.out.println("user2="+user2);
            String usercode = "ddms"+user2.getId();
            ddMemberService.updateCode(user_telephone, user_password, usercode);
            DdMember user3 = ddMemberService.sysrLogin(user_telephone, user_password);
            System.out.println("user3="+user3);
            if (user3 != null) {
                session.setAttribute("user_login", user3);
                return 0;
            }else {
                return 2;
            }
        }else {
            return 1;
        }
    }

    // 查询vip服务价格
    @ResponseBody
    @RequestMapping("fandMoney")
    public Ddrankingmonitor fandMoney(Integer serviceCycle, Integer type) {
        Ddrankingmonitor rankingMonitor = ddrankingmonitorService.fandMoney(serviceCycle, type);
        return rankingMonitor;
    }

    /**
     * 订单存库
     */
    @RequestMapping(value = { "/addOrder" })
    // @ResponseBody
    public String addOrder(HttpSession session, HttpServletRequest request) throws ParseException {
        String trade_no = (String) request.getAttribute("trade_no");// 支付宝交易号
        String out_trade_no = (String) request.getAttribute("out_trade_no");// 商户订单号
        String total_amount = (String) request.getAttribute("total_amount");// 付款金额
        Double order_money = Double.parseDouble(total_amount);
        String timestamp1 = (String) request.getAttribute("timestamp");// 交易日期
        Date start_time = null;
        Date end_time = null;
        start_time = DateUtils.format(timestamp1);
        // 计算会员结束日期
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(start_time);
        String body = (String) session.getAttribute("body");// 获取会员充值时间
        Integer time = Integer.parseInt(body);// 开通时长
        rightNow.add(Calendar.MONTH, time);// 日期加time个月
        end_time = rightNow.getTime();
        // 获取商品名称
        String subject = (String) session.getAttribute("subject");
        DdMember user = (DdMember) session.getAttribute("user_login");
        String telephone = user.getUserTelephone();

        DdOrders orders = new DdOrders(telephone, trade_no, out_trade_no, subject, time, order_money, start_time);
        System.out.println("查询订单前");
        // 先查询该订单是否已存在
        Integer lo = ddOrdersService.getOrdersByOrders(orders);
        System.out.println("查询订单后");
        if (lo == 0) {
            Integer i = ddOrdersService.addOrder(orders);
            System.out.println("添加订单");
            if (i == 1) {
                // 修改会员身份，并重新存session
                ddMemberService.updateUserType(telephone, 2);
                DdMember newUser = ddMemberService.sysrLogin(user.getUserTelephone(), user.getUserPassword());
                session.setAttribute("user_login", newUser);

                // 修改会员表(先查询，若没有记录，添加；有记录未到期，续期；有记录已过期，开通)
                DdViptime viptime = ddViptimeService.getVIPtimeByTelephoneAndType(telephone, subject);
                if (viptime == null) {
                    // 添加
                    DdViptime viptime1 = new DdViptime(telephone, subject, start_time, end_time);
                    ddViptimeService.addVIPtime(viptime1);
                } else {
                    DdViptime viptime1 = ddViptimeService.selectYN(telephone, subject);
                    if (viptime1 == null) {
                        // 开通
                        ddViptimeService.updateStart_End_time(telephone, start_time, end_time, subject);
                    } else {
                        // 续期
                        ddViptimeService.updateEnd_time(telephone, time, subject);
                    }
                }
                Integer days = ddViptimeService.selectDays(telephone);
                session.setAttribute("days", days);
                System.out.println("days="+days);

                //判断是否可以给父级金币
                Integer higher_id = user.getUserHigherId();
                System.out.println("腹肌id="+higher_id);
                if(higher_id!=null) {
                    if( higher_id!=0){
                        DdMember father = ddMemberService.selectTypeByID(higher_id);
                        Integer user_type = father.getUserType();
                        String f_phone = father.getUserTelephone();
                        if(user_type==2) {
                            ddMemberService.updateUser_gold(higher_id);
                            DdRebatelog urlog = new DdRebatelog(f_phone, telephone);
                            ddRebatelogService.insertUser_rebate_log(urlog);
                        }
                    }
                }
                return "redirect:/center";
            } else {
                return "/";
            }
        } else {
            return "/";
        }
    }

    /**
     * 验证图片验证码后发短信
     * @param picCode
     * @param session
     * @param phone
     * @param code
     * @return
     */
    @RequestMapping("checkPicCode")
    @ResponseBody
    public boolean checkPicCode(String picCode,HttpSession session,String phone, String code) {
        //String yanzhengma = session.getAttribute("code").toString();//获取session中图片验证码
        picCode = picCode.toUpperCase();
        Boolean mesg = false;
        if(picCode.equals(code)){
            try {
                //调用阿里短信sdk发送验证码
                AliCloudSMS.sendSms(phone, AliCloudSMS.getMsgCode(),session);
                mesg = true;
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        System.out.println("mesg="+mesg);
        return mesg;
    }

    /**
     * 检查登录
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkLogin")
    public Integer checkLogin(HttpSession session){
        DdMember ddMember = (DdMember)session.getAttribute("user_login");
        if(ddMember==null){
            return 0;
        }else {
            return 1;
        }
    }

    /**
     * 获取类目
     * @param parentId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getCats",method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    public Map getCats(@RequestParam String parentId){
        Map<String, Object> map = new HashMap<>();
        try {
            StringBuffer str = new StringBuffer();
            List<Map> l = PDDUtils.getGoodsCats(parentId);
            for (Map p:l) {
                str.append("<option value='"+p.get("opt_id")+"'>"+p.get("opt_name")+"</option>");
                //System.out.println("类别ID："+p.get("cat_id")+"~~~类别名称："+p.get("cat_name"));
            }
            map.put("cats",str);
            map.put("success","000");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取店铺商品列表
     * @param mallId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getMall",method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    public Map getMall(@RequestParam String mallId){
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map> l = PDDUtils.getMallList(mallId);
            for (Map p:l) {
                //System.out.println("类别ID："+p.get("cat_id")+"~~~类别名称："+p.get("cat_name"));
            }
            //map.put("cats",str);
            map.put("success","000");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取关键词
     * @param keyword
     * @param goodsId
     * @param sort
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getKey",method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    public Map getKey(String keyword, @RequestParam String goodsId, String sort, String parentId){
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map> list = PDDUtils.getGoodsList(keyword,sort,parentId);
            for (int i = 1; i < list.size(); i++) {
                Map p = list.get(i);
                if(goodsId.equals(String.valueOf(p.get("goods_id")))){
                    Map<String,Object> m = new HashMap<>();
                    m.put("name",p.get("goods_name"));
                    m.put("group",p.get("min_group_price"));
                    m.put("normal",p.get("min_normal_price"));
                    m.put("sold",p.get("sold_quantity"));
                    m.put("img",p.get("goods_thumbnail_url"));
                    m.put("top",i);
                    map.put("goods",m);
                    System.out.println("商品信息："+p.get("goods_name")+"~~~团购价："+p.get("min_group_price")+"~~~单价："+p.get("min_normal_price")+"~~~销量："+p.get("sold_quantity")+"~~~排名："+i);
                    break;
                }
            }
            map.put("success","000");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 根据VIP获取关键词
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getKeyVip",method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    public Map getKeyVip(@RequestParam int userId){
        Map<String, Object> map = new HashMap<>();
        try {
            Map<String,Object> m = ddKeyService.getList(userId);
            map.put("goods",m);
            map.put("success","000");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 添加关键词
     * @param userId
     * @param keyword
     * @param goodsId
     * @param sort
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "addKey",method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    public Map addKey(@RequestParam int userId,@RequestParam String keyword,@RequestParam String goodsId,@RequestParam String sort){
        Map<String, Object> map = new HashMap<>();
        try {
            int num = ddKeyService.getListSize(userId);
            if(num>10){
                map.put("success","001");
            }else{
                List<Map> list = PDDUtils.getGoodsList(keyword,sort,"");
                for (int i = 1; i < list.size(); i++) {
                    Map p = list.get(i);
                    if(p.get("goods_id").toString().equals(goodsId)){
                        DdKey ddKey = new DdKey();
                        ddKey.setGoodsKey(keyword);
                        ddKey.setGoodsId(p.get("goods_name").toString());
                        ddKey.setGoodsName(p.get("goods_name").toString());
                        ddKey.setGoodsSold(p.get("sold_quantity").toString());
                        ddKey.setGoodsThumbnail(p.get("goods_thumbnail_url").toString());
                        ddKey.setGroupPrice(p.get("min_group_price").toString());
                        ddKey.setNormalPrice(p.get("min_normal_price").toString());
                        ddKey.setGoodsTop(String.valueOf(i));
                        ddKey.setGoodsTime(new Date());
                        ddKeyService.save(ddKey);
                        System.out.println("商品信息："+p.get("goods_name")+"~~~团购价："+p.get("min_group_price")+"~~~单价："+p.get("min_normal_price")+"~~~销量："+p.get("sold_quantity")+"~~~排名："+i);
                        break;
                    }
                }
            }
            map.put("success","000");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
