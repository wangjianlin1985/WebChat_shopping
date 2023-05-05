var utils = require("../../utils/common.js")
var config = require("../../utils/config.js")

Page({
  /**
   * 页面的初始数据
   */
  data: {
    classId: 0, //类别id
    className: "", //类别名称
    classDesc: "", //类别描述
    loadingHide: true,
    loadingText: "网络操作中。。",
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (params) {
    var self = this
    var classId = params.classId
    var url = config.basePath + "api/productClass/get/" + classId
    utils.sendRequest(url, {}, function (productClassRes) {
      wx.stopPullDownRefresh()
      self.setData({
        classId: productClassRes.data.classId,
        className: productClassRes.data.className,
        classDesc: productClassRes.data.classDesc,
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

