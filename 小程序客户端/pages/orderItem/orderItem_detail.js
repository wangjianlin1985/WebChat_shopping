var utils = require("../../utils/common.js")
var config = require("../../utils/config.js")

Page({
  /**
   * 页面的初始数据
   */
  data: {
    itemId: 0, //条目id
    orderObj: "", //所属订单
    productObj: "", //订单商品
    price: "", //商品单价
    orderNumer: "", //购买数量
    loadingHide: true,
    loadingText: "网络操作中。。",
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (params) {
    var self = this
    var itemId = params.itemId
    var url = config.basePath + "api/orderItem/get/" + itemId
    utils.sendRequest(url, {}, function (orderItemRes) {
      wx.stopPullDownRefresh()
      self.setData({
        itemId: orderItemRes.data.itemId,
        orderObj: orderItemRes.data.orderObj.orderNo,
        productObj: orderItemRes.data.productObj.productName,
        price: orderItemRes.data.price,
        orderNumer: orderItemRes.data.orderNumer,
      })
    })
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
  }

})

