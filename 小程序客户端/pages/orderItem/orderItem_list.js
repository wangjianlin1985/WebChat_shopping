var utils = require("../../utils/common.js");
var config = require("../../utils/config.js");

Page({
  /**
   * 页面的初始数据
   */
  data: {
    orderNo: "",
    queryViewHidden: true, //是否隐藏查询条件界面
    orderObj_Index:"0", //所属订单查询条件
    orderInfos: [{"orderNo":"","orderNo":"不限制"}],
    productObj_Index:"0", //订单商品查询条件
    products: [{"productId":0,"productName":"不限制"}],
    orderItems: [], //界面显示的订单条目列表数据
    page_size: 8, //每页显示几条数据
    page: 1,  //当前要显示第几页
    totalPage: null, //总的页码数
    loading_hide: true, //是否隐藏加载动画
    nodata_hide: true, //是否隐藏没有数据记录提示
  },

  // 加载订单条目列表
  listOrderItem: function () {
    var self = this
    var url = config.basePath + "api/orderItem/list"
    //如果要显示的页码超过总页码不操作
    if(self.data.totalPage != null && self.data.page > self.data.totalPage) return
    self.setData({
      loading_hide: false,  //显示加载动画
    })
    //提交查询参数到服务器查询数据列表
    utils.sendRequest(url, {
      "orderObj.orderNo": self.data.orderNo, //self.data.orderInfos[self.data.orderObj_Index].orderNo,
      "productObj.productId": self.data.products[self.data.productObj_Index].productId,
      "page": self.data.page,
      "rows": self.data.page_size,
    }, function (res) { 
      wx.stopPullDownRefresh()
      setTimeout(function () {  
        self.setData({
          orderItems: self.data.orderItems.concat(res.data.list),
          totalPage: res.data.totalPage,
          loading_hide: true
        })
      }, 500)
      //如果当前显示的是最后一页，则显示没数据提示
      if(self.data.page == self.data.totalPage) {
        self.setData({
          nodata_hide: false,
        })
      }
    })
  },

  //显示或隐藏查询视图切换
  toggleQueryViewHide: function () {
    this.setData({
      queryViewHidden: !this.data.queryViewHidden,
    })
  },

  //点击查询按钮的事件
  queryOrderItem: function(e) {
    var self = this
    self.setData({
      page: 1,
      totalPage: null,
      orderItems: [],
      loading_hide: true, //隐藏加载动画
      nodata_hide: true, //隐藏没有数据记录提示 
      queryViewHidden: true, //隐藏查询视图
    })
    self.listOrderItem()
  },

  //绑定查询参数输入事件
  searchValueInput: function (e) {
    var id = e.target.dataset.id
  },

  //查询参数所属订单选择事件
  bind_orderObj_change: function(e) {
    this.setData({
      orderObj_Index: e.detail.value
    })
  },

  //查询参数订单商品选择事件
  bind_productObj_change: function(e) {
    this.setData({
      productObj_Index: e.detail.value
    })
  },

  init_query_params: function() { 
    var self = this
    var url = null
    //初始化所属订单下拉框
    url = config.basePath + "api/orderInfo/listAll"
    utils.sendRequest(url,null,function(res){
      wx.stopPullDownRefresh();
      self.setData({
        orderInfos: self.data.orderInfos.concat(res.data),
      })
    })
    //初始化订单商品下拉框
    url = config.basePath + "api/product/listAll"
    utils.sendRequest(url,null,function(res){
      wx.stopPullDownRefresh();
      self.setData({
        products: self.data.products.concat(res.data),
      })
    })
  },

  //删除订单条目记录
  removeOrderItem: function (e) {
    var self = this
    var itemId = e.currentTarget.dataset.itemid
    wx.showModal({
      title: '提示',
      content: '确定要删除itemId=' + itemId + '的记录吗？',
      success: function (sm) {
        if (sm.confirm) {
          // 用户点击了确定 可以调用删除方法了
          var url = config.basePath + "api/orderItem/delete/" + itemId
          utils.sendRequest(url, null, function (res) {
            wx.stopPullDownRefresh();
            wx.showToast({
              title: '删除成功',
              icon: 'success',
              duration: 500
            })
            //删除订单条目后客户端同步删除数据
            var orderItems = self.data.orderItems;
            for (var i = 0; i < orderItems.length; i++) {
              if (orderItems[i].itemId == itemId) {
                orderItems.splice(i, 1)
                break
              }
            }
            self.setData({ orderItems: orderItems })
          })
        } else if (sm.cancel) {
          console.log('用户点击取消')
        }
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({ orderNo: options.orderNo })
    this.listOrderItem()
    this.init_query_params()
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    var self = this
    self.setData({

      page: 1,  //显示最新的第1页结果
      orderItems: [], //清空订单条目数据
      nodata_hide: true, //隐藏没数据提示
    })
    self.listOrderItem()
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    var self = this
    if (self.data.page < self.data.totalPage) {
      self.setData({
        page: self.data.page + 1, 
      })
      self.listOrderItem()
    }
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }

})

