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

  //提交服务器修改支付方式信息
  formSubmit: function (e) {
    var self = this
    var formData = e.detail.value
    var url = config.basePath + "api/payWay/update"
    utils.sendRequest(url, formData, function (res) {
      wx.stopPullDownRefresh();
      wx.showToast({
        title: '修改成功',
        icon: 'success',
        duration: 500
      })

      //服务器修改成功返回列表页更新显示为最新的数据
      var pages = getCurrentPages()
      var payWaylist_page = pages[pages.length - 2];
      var payWays = payWaylist_page.data.payWays
      for(var i=0;i<payWays.length;i++) {
        if(payWays[i].payWayId == res.data.payWayId) {
          payWays[i] = res.data
          break
        }
      }
      payWaylist_page.setData({payWays:payWays})
      setTimeout(function () {
        wx.navigateBack({})
      }, 500) 
    })
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
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
  },

})

