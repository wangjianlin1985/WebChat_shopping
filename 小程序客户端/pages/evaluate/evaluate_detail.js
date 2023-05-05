var utils = require("../../utils/common.js")
var config = require("../../utils/config.js")

Page({
  /**
   * 页面的初始数据
   */
  data: {
    evaluateId: 0, //评价编号
    productObj: "", //商品名称
    userObj: "", //用户名
    content: "", //评价内容
    evaluateTime: "", //评价时间
    loadingHide: true,
    loadingText: "网络操作中。。",
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (params) {
    var self = this
    var evaluateId = params.evaluateId
    var url = config.basePath + "api/evaluate/get/" + evaluateId
    utils.sendRequest(url, {}, function (evaluateRes) {
      wx.stopPullDownRefresh()
      self.setData({
        evaluateId: evaluateRes.data.evaluateId,
        productObj: evaluateRes.data.productObj.productName,
        userObj: evaluateRes.data.userObj.name,
        content: evaluateRes.data.content,
        evaluateTime: evaluateRes.data.evaluateTime,
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

