var utils = require("../../utils/common.js")
var config = require("../../utils/config.js")

Page({
  /**
   * 页面的初始数据
   */
  data: {
    productId: 0, //商品id
    productClassObj: "", //商品类别
    productName: "", //商品名称
    mainPhotoUrl: "", //商品主图
    price: "", //商品价格
    productCount: "", //商量数量
    productDesc: "", //商品描述
    recommendFlag: "", //是否推荐
    hotNum: "", //人气值
    addTime: "", //上架时间
    loadingHide: true,
    loadingText: "网络操作中。。",
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
        productClassObj: productRes.data.productClassObj.className,
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
    })
  },
  
  viewEvaluate: function() {
    var self = this;  
    wx.navigateTo({
      url: '../evaluate/product_evaluate_list?productId=' + self.data.productId ,
    }) 
 
  },

  addCart: function() {
    var self = this; 
    var url = config.basePath + "api/shopCart/userAdd";
    utils.sendRequest(url, {"shopCart.productObj.productId": self.data.productId }, function (res) {
      wx.stopPullDownRefresh();
      var title = "加入购物车成功";
      console.log(res); 
      wx.showToast({
        title: title,
        icon: 'success',
        duration: 500
      }) 
    });
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

