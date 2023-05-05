var utils = require("../../utils/common.js");
var config = require("../../utils/config.js");

Page({
  /**
   * 页面的初始数据
   */
  data: {
    noticeId: 0, //公告id
    title: "", //标题
    content: "", //公告内容
    publishDate: "", //发布时间
    loadingHide: true,
    loadingText: "网络操作中。。",
  },

  //选择发布时间
  bind_publishDate_change: function (e) {
    this.setData({
      publishDate: e.detail.value
    })
  },
  //清空发布时间
  clear_publishDate: function (e) {
    this.setData({
      publishDate: "",
    });
  },

  /*提交添加新闻公告到服务器 */
  formSubmit: function (e) {
    var self = this;
    var formData = e.detail.value;
    var url = config.basePath + "api/notice/add";
    utils.sendRequest(url, formData, function (res) {
      wx.stopPullDownRefresh();
      wx.showToast({
        title: '发布成功',
        icon: 'success',
        duration: 500
      })

      //提交成功后重置表单数据
      self.setData({
        noticeId: 0,
        title: "",
        content: "",
        publishDate: "",
        loadingHide: true,
        loadingText: "网络操作中。。",
      })
    });
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
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

