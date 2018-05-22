<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en" ng-app="managerApp">
<head>
    <meta charset="UTF-8">
    <title>查询订单</title>
    <link rel="stylesheet/less" href="${ctx}/static/css/phoneReview.less" type="text/less">
    <link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
    <link rel="stylesheet" href="${ctx}/static/css/jquery-ui.css">
    <link rel="stylesheet" href="${ctx}/static/css/style.css">
   
    <script  charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery.min.js"></script>
    <script  charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/less.min.js"></script>
    <script  charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/angular.min.js"></script>
    <script  charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery-ui.js"></script>
    <script  charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/ui-bootstrap-tpls.js"></script>
    <script  charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/moment.js"></script>
    <script  charset="utf-8" type="text/javascript" src="${ctx}/static/dumai/global/js/utils.js"></script>
    <script  charset="utf-8" type="text/javascript" src="${ctx}/static/dumai/auditing/js/distributionTaskList.js"></script>
</head>
<body style="background:#f6f6f6" ng-controller="managerCtrl">
    <div class="managerContain" id="managerContain" >
        <div class="leftList">
            <div class="leftList_top"></div>
            <div class="leftList_bottom">
                <ul>
                    <li ><i class="icon icon_task elvesIcon"></i><a href="${ctx}/auditingTask/toDistribution.do">分配电核任务</a></li>
                    <li class="current"><i class="icon icon_order elvesIcon"></i><a href="${ctx}/auditingTask/toOrderList.do">查询订单</a></li>
                    <li><i class="icon icon_power elvesIcon"></i><a href="${ctx}/auth/toList.do">鉴权结果</a></li>
                    <li><i class="icon icon_power elvesIcon"></i><a href="${ctx}/auth/toAuth.do">身份验证</a></li>
                </ul>
            </div>
        </div>
        <div class="rightContain">
            <div class="rightContain_title">
                <div class="title">电核任务列表 > <em>查询订单</em></div>
                <div class="exit"><span>欢迎您,</span><em ng-bind="username"></em><a  href="${ctx}">[退出]</a></div>
            </div>
            <div class="rightContain_bodies">
                <div class="conditions">
                    <ul>
                        <li>审核人：
                            <select ng-Model="disValue" ng-change="changeDatasName()" >
								<option ng-repeat="distributionList in distributionLists"  ng-bind="distributionList.surname" value="{{distributionList.code}}" class="aa"></option>
							</select>
                        </li>
                        <%--<li>业务类型：--%>
                            <%--<select ng-Model="typeValue" ng-change="changeDatasType()" >--%>
								<%--<option ng-repeat="typeList in typeLists"  ng-bind="typeList.name" value="{{typeList.code}}"></option>--%>
							<%--</select>--%>
                        <%--</li>--%>
                        <li>审核状态：
                            <select ng-Model="typeStatus">
                            	<option value="">全部</option>
                            	<option value="0">待审核</option>
                                <option value="1">通过</option>
                                <option value="2">拒绝</option>
                                <option value="3">处理中</option>
                            </select>
                        </li>
                        <li>客户姓名：
                            <input type="text"  ng-model="name" style="height:28px;outline:none;border: 1px solid #d8d8d8;">
                        </li>
                       <li>订单生成日期：
                            <label><input type="text" id="datepicker1" ng-model="date1"><em class="elvesIcon"></em></label>
                        </li>
                                                                       至
                        <li>
                            <label><input type="text" id="datepicker2" ng-model="date2"><em class="elvesIcon"></em></label>
                        </li>
                        <li>
                            <span class="clearCon elvesIcon" ng-click="clear()"></span><span class="demand elvesIcon" ng-click="askData()"></span>
                        </li>
                    </ul>
                </div>
                <table class="table table-bordered" >
                    <thead>
                    <tr>
                        <th></th>
                        <th>订单生成日期</th>
                        <th>客户姓名</th>
                        <th>贷款金额</th>
                        <%--<th>贷款金额业务类型</th>--%>
                        <th>机构名称</th>
                        <th>审核人</th>
                        <th>审核状态</th>
                        <th>响应时间（分:秒）</th>
                        <th>受理时间（分:秒）</th>
                        <%--<th>异常信息</th>--%>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                        <tr ng-repeat="item in managerLists">
                        <td ng-bind="{{orderNumber}}+$index+1"></td>
                        <td ng-bind="item.create_time"></td>
                        <td ng-bind="item.customer_name"></td>
                        <td ng-bind="item.sqje"></td>
                        <%--<td ng-bind="item.typeName"></td>--%>
                        <td ng-bind="item.organization"></td>
                        <td ng-bind="item.surname"></td>
                        <td ng-bind="item.status"></td>
                        <td ng-bind="item.response_time"></td>
                        <td ng-bind="item.handle_time"></td>
                        <%--<td ng-bind="item.short_exception" width="20%" title="{{item.other_exception}}"></td>--%>
                        <td><a href="javascript:;" ng-click="dhPage(item.code,item.playLook)" ng-bind="item.playLook" title="{{item.titlePlay}}"></a></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        	 <section class="row pageDemo" >
         		<section style="margin:0px auto;">
            		<pagination  total-items="totalItems" ng-model="currentPage" class="pagination-sm"  max-size="maxSize" previous-text="〈" next-text="〉"  rotate="false" ng-change="getPageLists()"></pagination>
           		</section>
       		</section>
        </div>
    </div>
    <div class="infoReportdh">
	  <div class="infoReport2">
		<h4>个人详细报告 <span ng-click="closeReport()"></span></h4>
          <ul>
            <li><span>姓名：</span><span ng-bind="basics.name"></span></li>
            <li><span>性别：</span><span ng-bind="basics.sex"></span></li>
            <li><span>年龄：</span><span ng-bind="basics.age"></span></li>
            <li><span>婚姻：</span><span ng-bind="basics.married"></span></li>
            <li><span>学历：</span><span ng-bind="basics.education"></span></li>
            <li><span>身份证号码：</span><span ng-bind="basics.card_num"></span></li>
            <li><span>手机号码：</span><span ng-bind="basics.mobile"></span></li>
            <li><span>银行卡：</span><span ng-bind="basics.bank_num"></span></li>
            <li><span>常住地址：</span><span ng-bind="basics.address"></span></li>
            <li><span>籍贯：</span><span ng-bind="birthplace"></span></li>
            <li><span>业务类型 ：</span><span ng-bind="basics.typename"></span></li>
            <li><span>借款金额 ：	</span><span ng-bind="basics.sqje"></span></li>
            <li><span>借款期限 ：	</span><span ng-bind="basics.Jkqx"></span></li>
            <li><span>家庭住址 ：	</span><span ng-bind="homeAddress"></span></li>
            <li><span>职业情况 ：</span><span ng-bind="basics.profession"></span></li>
            <li><span>固定收入 ：</span><span ng-bind="basics.income"></span></li>
            <li><span>紧急联系人1：</span><span style="width:15%;" ng-bind="basics.linkname1"></span><span style="width:20%;" ng-bind="basics.linkphone1"></span><span style="width:15%;"  ng-bind="basics.linkReation1"></span></li>
            <li><span>紧急联系人2：</span><span style="width:15%;" ng-bind="basics.linkname2"></span><span style="width:20%;" ng-bind="basics.linkphone2"></span><span style="width:15%;"  ng-bind="basics.linkReation2"></span></li>
            <ul class="generated">
                <li ng-repeat="aa in arrays"><span ng-cloak>{{aa.titleName}} ：</span><span>{{aa.titleContent}}</span></li>
            </ul>
          </ul>
         <img data-ng-src="data:image/gif;base64,{{perImg}}" alt={{basics.name}}{{perImg}} ng-cloak>
	  </div>
	</div>
	<div class="reviewContain_tip" id="reviewContain_tip" style="display:none;">
      <div class="mainReview">
        <div class="header">
            <div class="exit exit_icon"><span ng-click="close()"></span></div>
        </div>
        <div class="clearfix"></div>
        <div class="bodies">
            <div class="info_top">
                <div class="top_left">
                    <table class="table table-bordered" style="margin-bottom:0px;">
                        <tbody>
                        <tr>
                            <td><span>姓名：</span><span ng-bind="orderInfo.name"></span></td>
                            <td><span>身份证号：</span><span ng-bind="orderInfo.card_num"></span></td>
                            <td><span>银行卡号：</span><span ng-bind="orderInfo.bank_num"></span></td>
                            <td><span>电话：</span><span ng-bind="orderInfo.mobile"></span></td>
                        </tr>
                        </tbody>
                    </table>
                    <table class="table table-bordered">
                        <tbody>
                        <tr>
                            <td><span>机构名称：</span><span ng-bind="orderInfo.organization"></span></td>
                            <td><span>业务类型：</span><span ng-bind="orderInfo.typeName"></span></td>
                            <td><span>贷款金额：</span><span ng-bind="orderInfo.sqje"></span></td>
                            <td><span>贷款期限：</span><span ng-bind="orderInfo.Jkqx"></span></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="top_right" ng-click="infoReport(orderInfo.code)">
                    <a href="javascript:;" >查看个人详细信息</a>
                </div>
            </div>
            <div class="clearfix"></div>
              <div class="info_middle">
            <h5><span></span>电核信息</h5>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>电核项</th>
                    <th>描述</th>
                    <th>结果</th>
                    <th>是否命中</th>
                    <th>备注</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in phoneLists">
                    <td ng-bind="item.manager_item_name"></td><td ng-bind="item.dh_description"></td><td ng-bind="item.auto_result"></td>
                    <td ng-bind="item.result"></td>
                    <td ng-bind="item.remark"></td>
                </tr>
                </tbody>
            </table>
          </div>
          <div class="info_bottom">
            <h5><span></span>增项信息</h5>
            <table class="table table-bordered">
              <thead>
              <tr>
                  <th>电核项</th>
                  <th>描述</th>
                  <th>是否命中</th>
                  <th>备注</th>
              </tr>
              </thead>
              <tbody>
                   <tr ng-repeat="item in addManagerLists">
                      <td ng-bind="item.item_name"></td><td ng-bind="item.item_value"></td>
                       <td ng-bind="item.dh_content"></td><td ng-bind="item.remark"></td> 
                    </tr>
                 </tbody>
              </table>
              <div class="text_content_look">
                  <div><span>电核基本情况：</span><span ng-bind="dhBaseInfo.description"></span></div>
                    <div><span>异常情况：</span><span ng-bind="dhBaseInfo.other_exception"></span></div>
                </div>
          </div>
        </div>
      </div>
    </div>
</body>
</html>