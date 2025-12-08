const home = [
    {
        //注意：path必须跟pages.json中的地址对应，最前面别忘了加'/'哦
        path: "/pages/index/index",
        aliasPath: "/", //对于h5端你必须在首页加上aliasPath并设置为/
        name: "index",
        meta: {
            title: "首页",
        },
    },
    {
        //注意：path必须跟pages.json中的地址对应，最前面别忘了加'/'哦
        path: "/pages/mine/examination/examination",
        aliasPath: "/", //对于h5端你必须在首页加上aliasPath并设置为/
        name: "examination",
        meta: {
            title: "我的报考",
        },
    },
    {
        path: "/pages/mine/examination/examinationEmpty",
        aliasPath: "/", //对于h5端你必须在首页加上aliasPath并设置为/
        name: "examinationEmpty",
        meta: {
            title: "取消报考",
        },
    },
    {
        path: "/pages/mine/examination/examinationCancel",
        aliasPath: "/", //对于h5端你必须在首页加上aliasPath并设置为/
        name: "examinationCancel",
        meta: {
            title: "取消报考",
        },
    },
    {
        path: "/pages/mine/allowance/allowanceApplication",
        aliasPath: "/", //对于h5端你必须在首页加上aliasPath并设置为/
        name: "allowanceApplication",
        meta: {
            title: "申请补贴",
        },
    },
    {
        path: "/pages/mine/allowance/choiceIdcard",
        aliasPath: "/", //对于h5端你必须在首页加上aliasPath并设置为/
        name: "choiceIdcard",
        meta: {
            title: "选择银行卡",
        },
    },
    {
        path: "/pages/mine/allowance/addIdcard",
        aliasPath: "/", //对于h5端你必须在首页加上aliasPath并设置为/
        name: "addIdcard",
        meta: {
            title: "添加银行卡",
        },
    },
    {
        path: "/pages/mine/allowance/idcardComplete",
        aliasPath: "/", //对于h5端你必须在首页加上aliasPath并设置为/
        name: "idcardComplete",
        meta: {
            title: "添加银行卡",
        },
    },
    {
        path: "/pages/mine/allowance/allowanceProgress",
        aliasPath: "/", //对于h5端你必须在首页加上aliasPath并设置为/
        name: "allowanceProgress",
        meta: {
            title: "补贴进度",
        },
    },
	{
        path: "pages/mine/order/order",
        aliasPath: "/", //对于h5端你必须在首页加上aliasPath并设置为/
        name: "order",
        meta: {
            title: "题库订单",
        },
    },
	{
        path: "pages/mine/order/order",
        aliasPath: "/", //对于h5端你必须在首页加上aliasPath并设置为/
        name: "order",
        meta: {
            title: "题库订单",
        },
    },
	{
        path: "pages/mine/order/orderList",
        aliasPath: "/", //对于h5端你必须在首页加上aliasPath并设置为/
        name: "orderList",
        meta: {
            title: "订单列表",
        },
    },
	{
        path: "pages/mine/order/orderDetail",
        aliasPath: "/", //对于h5端你必须在首页加上aliasPath并设置为/
        name: "orderDetail",
        meta: {
            title: "订单详情",
        },
    },
	{
        path: "pages/mine/order/orderPay",
        aliasPath: "/", //对于h5端你必须在首页加上aliasPath并设置为/
        name: "orderPay",
        meta: {
            title: "订单支付",
        },
    },
	{
        path: "pages/mine/order/orderPayComplete",
        aliasPath: "/", //对于h5端你必须在首页加上aliasPath并设置为/
        name: "orderPayComplete",
        meta: {
            title: "支付成功",
        },
    },
];
export default home;
