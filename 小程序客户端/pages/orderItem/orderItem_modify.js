var utils = require("../../utils/common.js")
var config = require("../../utils/config.js")

Page({
  /**
   * 页面的初始数据
   */
  data: {
    itemId: 0, //条目id
    orderObj_Index: "0", //所属订单
    orderInfos: [],
    productObj_Index: "0", //订单商品
    products: [],
    price: "", //商品单价
    orderNumer: "", //购买数量
    loadingHide: true,
    loadingText: "网络操作中。。",
  },

  //所属订单修改事件
  bind_orderObj_change: function (e) {
    this.setData({
      orderObj_Index: e.detail.value
    })
  },

  //订单商品修改事件
  bind_productObj_change: function (e) {
    this.setData({
      productObj_Index: e.detail.value
    })
  },

  //提交服务器修改订单条目信息
  formSubmit: function (e) {
    var self = this
    var formData = e.detail.value
    var url = config.basePath + "api/orderItem/update"
    utils.sendRequest(url, formData, function (res) {
      wx.stopPullDownRefresh();
      wx.showToast({
        title: '修改成功',
        icon: 'success',
        duration: 500
      })

      //服务器修改成功返回列表页更新显示为最新的数据
      var pages = getCurrentPages()
      var orderItemlist_page = pages[pages.length - 2];
      var orderItems = orderItemlist_page.data.orderItems
      for(var i=0;i<orderItems.length;i++) {
        if(orderItems[i].itemId == res.data.itemId) {
          orderItems[i] = res.data
          break
        }
      }
      orderItemlist_page.setData({orderItems:orderItems})
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
    var itemId = params.itemId
    var url = config.basePath + "api/orderItem/get/" + itemId
    utils.sendRequest(url, {}, function (orderItemRes) {
      wx.stopPullDownRefresh()
      self.setData({
        itemId: orderItemRes.data.itemId,
        orderObj_Index: 1,
        productObj_Index: 1,
        price: orderItemRes.data.price,
        orderNumer: orderItemRes.data.orderNumer,
      })

      var orderInfoUrl = config.basePath + "api/orderInfo/listAll" 
      utils.sendRequest(orderInfoUrl, null, function (res) {
        wx.stopPullDownRefresh()
        self.setData({
          orderInfos: res.data,
        })
        for (var i = 0; i < self.data.orderInfos.length; i++) {
          if (orderItemRes.data.orderObj.orderNo == self.data.orderInfos[i].orderNo) {
            self.setData({
              orderObj_Index: i,
            });
            break;
          }
        }
      })
      var productUrl = config.basePath + "api/product/listAll" 
      utils.sendRequest(productUrl, null, function (res) {
        wx.stopPullDownRefresh()
        self.setData({
          products: res.data,
        })
        for (var i = 0; i < self.data.products.length; i++) {
          if (orderItemRes.data.productObj.productId == self.data.products[i].productId) {
            self.setData({
              productObj_Index: i,
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

