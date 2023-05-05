var utils = require("../../utils/common.js")
var config = require("../../utils/config.js")

Page({
  /**
   * 页面的初始数据
   */
  data: {
    evaluateId: 0, //评价编号
    productObj_Index: "0", //商品名称
    products: [],
    userObj_Index: "0", //用户名
    userInfos: [],
    content: "", //评价内容
    evaluateTime: "", //评价时间
    loadingHide: true,
    loadingText: "网络操作中。。",
  },

  //商品名称修改事件
  bind_productObj_change: function (e) {
    this.setData({
      productObj_Index: e.detail.value
    })
  },

  //用户名修改事件
  bind_userObj_change: function (e) {
    this.setData({
      userObj_Index: e.detail.value
    })
  },

  //提交服务器修改商品评价信息
  formSubmit: function (e) {
    var self = this
    var formData = e.detail.value
    var url = config.basePath + "api/evaluate/update"
    utils.sendRequest(url, formData, function (res) {
      wx.stopPullDownRefresh();
      wx.showToast({
        title: '修改成功',
        icon: 'success',
        duration: 500
      })

      //服务器修改成功返回列表页更新显示为最新的数据
      var pages = getCurrentPages()
      var evaluatelist_page = pages[pages.length - 2];
      var evaluates = evaluatelist_page.data.evaluates
      for(var i=0;i<evaluates.length;i++) {
        if(evaluates[i].evaluateId == res.data.evaluateId) {
          evaluates[i] = res.data
          break
        }
      }
      evaluatelist_page.setData({evaluates:evaluates})
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
    var evaluateId = params.evaluateId
    var url = config.basePath + "api/evaluate/get/" + evaluateId
    utils.sendRequest(url, {}, function (evaluateRes) {
      wx.stopPullDownRefresh()
      self.setData({
        evaluateId: evaluateRes.data.evaluateId,
        productObj_Index: 1,
        userObj_Index: 1,
        content: evaluateRes.data.content,
        evaluateTime: evaluateRes.data.evaluateTime,
      })

      var productUrl = config.basePath + "api/product/listAll" 
      utils.sendRequest(productUrl, null, function (res) {
        wx.stopPullDownRefresh()
        self.setData({
          products: res.data,
        })
        for (var i = 0; i < self.data.products.length; i++) {
          if (evaluateRes.data.productObj.productId == self.data.products[i].productId) {
            self.setData({
              productObj_Index: i,
            });
            break;
          }
        }
      })
      var userInfoUrl = config.basePath + "api/userInfo/listAll" 
      utils.sendRequest(userInfoUrl, null, function (res) {
        wx.stopPullDownRefresh()
        self.setData({
          userInfos: res.data,
        })
        for (var i = 0; i < self.data.userInfos.length; i++) {
          if (evaluateRes.data.userObj.user_name == self.data.userInfos[i].user_name) {
            self.setData({
              userObj_Index: i,
            });
            break;
          }
        }
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

