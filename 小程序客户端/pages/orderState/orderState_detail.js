var utils = require("../../utils/common.js")
var config = require("../../utils/config.js")

Page({
  /**
   * 页面的初始数据
   */
  data: {
    stateId: 0, //状态编号
    stateName: "", //订单状态名称
    loadingHide: true,
    loadingText: "网络操作中。。",
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (params) {
    var self = this
    var stateId = params.stateId
    var url = config.basePath + "api/orderState/get/" + stateId
    utils.sendRequest(url, {}, function (orderStateRes) {
      wx.stopPullDownRefresh()
      self.setData({
        stateId: orderStateRes.data.stateId,
        stateName: orderStateRes.data.stateName,
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

