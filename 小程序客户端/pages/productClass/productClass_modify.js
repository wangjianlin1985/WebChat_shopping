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

  //提交服务器修改商品类别信息
  formSubmit: function (e) {
    var self = this
    var formData = e.detail.value
    var url = config.basePath + "api/productClass/update"
    utils.sendRequest(url, formData, function (res) {
      wx.stopPullDownRefresh();
      wx.showToast({
        title: '修改成功',
        icon: 'success',
        duration: 500
      })

      //服务器修改成功返回列表页更新显示为最新的数据
      var pages = getCurrentPages()
      var productClasslist_page = pages[pages.length - 2];
      var productClasss = productClasslist_page.data.productClasss
      for(var i=0;i<productClasss.length;i++) {
        if(productClasss[i].classId == res.data.classId) {
          productClasss[i] = res.data
          break
        }
      }
      productClasslist_page.setData({productClasss:productClasss})
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

