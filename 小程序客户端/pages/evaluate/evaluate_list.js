var utils = require("../../utils/common.js");
var config = require("../../utils/config.js");

Page({
  /**
   * 页面的初始数据
   */
  data: {
    queryViewHidden: true, //是否隐藏查询条件界面
    productObj_Index:"0", //商品名称查询条件
    products: [{"productId":0,"productName":"不限制"}],
    userObj_Index:"0", //用户名查询条件
    userInfos: [{"user_name":"","name":"不限制"}],
    evaluates: [], //界面显示的商品评价列表数据
    page_size: 8, //每页显示几条数据
    page: 1,  //当前要显示第几页
    totalPage: null, //总的页码数
    loading_hide: true, //是否隐藏加载动画
    nodata_hide: true, //是否隐藏没有数据记录提示
  },

  // 加载商品评价列表
  listEvaluate: function () {
    var self = this
    var url = config.basePath + "api/evaluate/list"
    //如果要显示的页码超过总页码不操作
    if(self.data.totalPage != null && self.data.page > self.data.totalPage) return
    self.setData({
      loading_hide: false,  //显示加载动画
    })
    //提交查询参数到服务器查询数据列表
    utils.sendRequest(url, {
      "productObj.productId": self.data.products[self.data.productObj_Index].productId,
      "userObj.user_name": self.data.userInfos[self.data.userObj_Index].user_name,
      "page": self.data.page,
      "rows": self.data.page_size,
    }, function (res) { 
      wx.stopPullDownRefresh()
      setTimeout(function () {  
        self.setData({
          evaluates: self.data.evaluates.concat(res.data.list),
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
  queryEvaluate: function(e) {
    var self = this
    self.setData({
      page: 1,
      totalPage: null,
      evaluates: [],
      loading_hide: true, //隐藏加载动画
      nodata_hide: true, //隐藏没有数据记录提示 
      queryViewHidden: true, //隐藏查询视图
    })
    self.listEvaluate()
  },

  //绑定查询参数输入事件
  searchValueInput: function (e) {
    var id = e.target.dataset.id
  },

  //查询参数商品名称选择事件
  bind_productObj_change: function(e) {
    this.setData({
      productObj_Index: e.detail.value
    })
  },

  //查询参数用户名选择事件
  bind_userObj_change: function(e) {
    this.setData({
      userObj_Index: e.detail.value
    })
  },

  init_query_params: function() { 
    var self = this
    var url = null
    //初始化商品名称下拉框
    url = config.basePath + "api/product/listAll"
    utils.sendRequest(url,null,function(res){
      wx.stopPullDownRefresh();
      self.setData({
        products: self.data.products.concat(res.data),
      })
    })
    //初始化用户名下拉框
    url = config.basePath + "api/userInfo/listAll"
    utils.sendRequest(url,null,function(res){
      wx.stopPullDownRefresh();
      self.setData({
        userInfos: self.data.userInfos.concat(res.data),
      })
    })
  },

  //删除商品评价记录
  removeEvaluate: function (e) {
    var self = this
    var evaluateId = e.currentTarget.dataset.evaluateid
    wx.showModal({
      title: '提示',
      content: '确定要删除evaluateId=' + evaluateId + '的记录吗？',
      success: function (sm) {
        if (sm.confirm) {
          // 用户点击了确定 可以调用删除方法了
          var url = config.basePath + "api/evaluate/delete/" + evaluateId
          utils.sendRequest(url, null, function (res) {
            wx.stopPullDownRefresh();
            wx.showToast({
              title: '删除成功',
              icon: 'success',
              duration: 500
            })
            //删除商品评价后客户端同步删除数据
            var evaluates = self.data.evaluates;
            for (var i = 0; i < evaluates.length; i++) {
              if (evaluates[i].evaluateId == evaluateId) {
                evaluates.splice(i, 1)
                break
              }
            }
            self.setData({ evaluates: evaluates })
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
    this.listEvaluate()
    this.init_query_params()
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    var self = this
    self.setData({
      page: 1,  //显示最新的第1页结果
      evaluates: [], //清空商品评价数据
      nodata_hide: true, //隐藏没数据提示
    })
    self.listEvaluate()
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
      self.listEvaluate()
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

