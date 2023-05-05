var utils = require("../../utils/common.js");
var config = require("../../utils/config.js");

Page({
  /**
   * 页面的初始数据
   */
  data: {
    itemId: 0, //条目id
    orderObj_Index: "0", //所属订单
    orderInfos: [],
    productObj_Index: "0", //订单商品
    products: [],
    price: "", //商品单价
    orderNumer: "", //购买数量
    loadingHide: true,
    loadingText: "网络操作中。。",
  },

  //初始化下拉框的信息
  init_select_params: function (options) { 
    var self = this;
    var url = null;
    url = config.basePath + "api/orderInfo/listAll";
    utils.sendRequest(url, null, function (res) {
      wx.stopPullDownRefresh();
      self.setData({
        orderInfos: res.data,
      });
    });
    url = config.basePath + "api/product/listAll";
    utils.sendRequest(url, null, function (res) {
      wx.stopPullDownRefresh();
      self.setData({
        products: res.data,
      });
    });
  },
  //所属订单改变事件
  bind_orderObj_change: function (e) {
    this.setData({
      orderObj_Index: e.detail.value
    })
  },

  //订单商品改变事件
  bind_productObj_change: function (e) {
    this.setData({
      productObj_Index: e.detail.value
    })
  },

  /*提交添加订单条目到服务器 */
  formSubmit: function (e) {
    var self = this;
    var formData = e.detail.value;
    var url = config.basePath + "api/orderItem/add";
    utils.sendRequest(url, formData, function (res) {
      wx.stopPullDownRefresh();
      wx.showToast({
        title: '发布成功',
        icon: 'success',
        duration: 500
      })

      //提交成功后重置表单数据
      self.setData({
        itemId: 0,
        orderObj_Index: "0",
        productObj_Index: "0",
        price: "",
        orderNumer: "",
        loadingHide: true,
        loadingText: "网络操作中。。",
      })
    });
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.init_select_params(options);
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var token = wx.getStorageSync('authToken');
    if (!token) {
      wx.navigateTo({
        url: '../mobile/mobile',
      })
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
  }
})

