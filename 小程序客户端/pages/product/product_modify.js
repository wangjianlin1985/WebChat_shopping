var utils = require("../../utils/common.js")
var config = require("../../utils/config.js")

Page({
  /**
   * 页面的初始数据
   */
  data: {
    productId: 0, //商品id
    productClassObj_Index: "0", //商品类别
    productClasss: [],
    productName: "", //商品名称
    mainPhoto: "upload/NoImage.jpg", //商品主图
    mainPhotoUrl: "",
    mainPhotoList: [],
    price: "", //商品价格
    productCount: "", //商量数量
    productDesc: "", //商品描述
    recommendFlag: "", //是否推荐
    hotNum: "", //人气值
    addTime: "", //上架时间
    loadingHide: true,
    loadingText: "网络操作中。。",
  },

  //选择上架时间事件
  bind_addTime_change: function (e) {
    this.setData({
      addTime: e.detail.value
    })
  },
  //清空上架时间事件
  clear_addTime: function (e) {
    this.setData({
      addTime: "",
    });
  },

  //选择商品主图上传
  select_mainPhoto: function (e) {
    var self = this
    wx.chooseImage({
      count: 1,   //可以上传一张图片
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      success: function (res) {
        var tempFilePaths = res.tempFilePaths
        self.setData({
          mainPhotoList: tempFilePaths,
          loadingHide: false, 
        });

        utils.sendUploadImage(config.basePath + "upload/image", tempFilePaths[0], function (res) {
          wx.stopPullDownRefresh()
          setTimeout(function () {
            self.setData({
              mainPhoto: res.data,
              loadingHide: true
            })
          }, 200)
        })
      }
    })
  },

  //商品类别修改事件
  bind_productClassObj_change: function (e) {
    this.setData({
      productClassObj_Index: e.detail.value
    })
  },

  //提交服务器修改商品信息
  formSubmit: function (e) {
    var self = this
    var formData = e.detail.value
    var url = config.basePath + "api/product/update"
    utils.sendRequest(url, formData, function (res) {
      wx.stopPullDownRefresh();
      wx.showToast({
        title: '修改成功',
        icon: 'success',
        duration: 500
      })

      //服务器修改成功返回列表页更新显示为最新的数据
      var pages = getCurrentPages()
      var productlist_page = pages[pages.length - 2];
      var products = productlist_page.data.products
      for(var i=0;i<products.length;i++) {
        if(products[i].productId == res.data.productId) {
          products[i] = res.data
          break
        }
      }
      productlist_page.setData({products:products})
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
    var productId = params.productId
    var url = config.basePath + "api/product/get/" + productId
    utils.sendRequest(url, {}, function (productRes) {
      wx.stopPullDownRefresh()
      self.setData({
        productId: productRes.data.productId,
        productClassObj_Index: 1,
        productName: productRes.data.productName,
        mainPhoto: productRes.data.mainPhoto,
        mainPhotoUrl: productRes.data.mainPhotoUrl,
        price: productRes.data.price,
        productCount: productRes.data.productCount,
        productDesc: productRes.data.productDesc,
        recommendFlag: productRes.data.recommendFlag,
        hotNum: productRes.data.hotNum,
        addTime: productRes.data.addTime,
      })

      var productClassUrl = config.basePath + "api/productClass/listAll" 
      utils.sendRequest(productClassUrl, null, function (res) {
        wx.stopPullDownRefresh()
        self.setData({
          productClasss: res.data,
        })
        for (var i = 0; i < self.data.productClasss.length; i++) {
          if (productRes.data.productClassObj.classId == self.data.productClasss[i].classId) {
            self.setData({
              productClassObj_Index: i,
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

