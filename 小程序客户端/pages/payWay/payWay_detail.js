var utils = require("../../utils/common.js")
var config = require("../../utils/config.js")

Page({
  /**
   * 页面的初始数据
   */
  data: {
    payWayId: 0, //支付方式id
    payWayName: "", //支付方式名称
    loadingHide: true,
    loadingText: "网络操作中。。",
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (params) {
    var self = this
    var payWayId = params.payWayId
    var url = config.basePath + "api/payWay/get/" + payWayId
    utils.sendRequest(url, {}, function (payWayRes) {
      wx.stopPullDownRefresh()
      self.setData({
        payWayId: payWayRes.data.payWayId,
        payWayName: payWayRes.data.payWayName,
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

