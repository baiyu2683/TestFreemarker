<#setting number_format="computer">
<#assign base=request.contextPath />
<!doctype html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>微信统计</title>
	<link href="/css/bootstrap.min.css" rel="stylesheet" type="text/css">
	<link href="/css/css.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div class="wrapper">
		<div class="container-fulid">
			<nav class="nav navbar-inverse navbar-fixed-top">
				<div class="container">
					<div class="navbar-header">
						<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myCollapse">
								<span class="icon-bar"></span>
								<span class="icon-bar"></span>
								<span class="icon-bar"></span>
							</button>
							<a href="#" class="navbar-brand hidden-lg hidden-md  hidden-sm visible-xs-block" >微信统计</a>
					</div>
					<div id="myCollapse" class="collapse navbar-collapse">
						<ul class="nav navbar-nav">
							<li class="menuItem active">
								<a href="#article">文章的对比</a>
							</li>
							<li class="menuItem">
								<a href="#common">公众号对比</a>
							</li>
							<li class="menuItem">
								<a href="#caigou">TRS和采购公众号平均每天发文数量对比</a>
							</li>
						</ul>
					</div>	
				</div>
			</nav>	
		</div>
		<!-- 监听的内容区域 -->
		<!-- 文章的对比部分开始 -->
		<div class="container" style="position:relative;margin-top:50px;">
		<div id="article">
			<!-- 4月份TRS与采购文章数对比-->
			<div id="main1" style="width:100%; height:400px"></div>
			<p>描述：TRS和采购每日采集入库数据量的对比图。</p>
			
			<!-- TRS与采购每日数据异同 -->
			<div id="main2" style="width:100%; height:400px"></div>
			<p>描述：查看TRS和采购每日采集数据的交集、TRS独有数据和采购独有数据的数据量以及数据比例的变化趋势。</p>
			<!-- 4/12-4/21 TRS和采购数据异同 -->
			<div id="main3" style="height:500px" ></div>
			<p>描述：查看一段时间内采集数据的交集、TRS独有数据和采购独有数据的整体比例。</p>
			<!-- 微信采集某日发布数据所需天数对比图 -->
			<div id="main4" style="width:100%; height:600px"></div>
			<p>描述：将TRS和采购每日采集的数据，按照采集到文章需要的天数进行统计和对比。</p>
            <#--<!-- 微信采集某日发布数据从发布到采集所需时间 &ndash;&gt;-->
            <#--<div id="main11" style="width:100%; height:500px"></div>-->
            <#--<p>描述：将TRS和采购当日采集的数据，按照发布到采集的时间进行统计对比</p>-->
			<!-- 每日采集数据当天和历史文章量对比图 -->
			<div id="main5" style="width:100%; height:400px"></div>
			<p>描述：将TRS和采购每日采集的数据，按照采集当日发布数据和采集历史发布数据进行对比。</p>

		</div>
		<!--文章的对比部分结束  -->
		<!--公众号对比部分开始  -->

		<div id="common">
			<!-- 每日采集文章的微信公众号数对比 -->
			<div id="main6" style="width:100%; height:400px"></div>
			<p>描述：将TRS和采购每日采集到发文的微信公众号数量进行对比。</p>
			
			<!-- 4/12-4/21 TRS和采购微信公众号对比 -->
			<div id="main7" style="width:100%; height:400px"></div>
			<p>描述：将TRS和采购每日发布文章的微信公众号数量进行对比。</p>
			<!-- 4/12-4/21 TRS和采购微信公众号差异对比 -->
			<div id="main8" style="width:100%; height:400px"></div>
			<p>描述：查看TRS和采购每日采集到文章的微信公众号的交集、TRS独有和采购独有的微信公众号数据，并查看采集到微信公众号数量的变化趋势。
</p>
			<!-- 4/12-4/21 TRS和采购微信公众号异同 -->
        
			<div id="main9" style="width:100%; height:500px"></div>
			<p>描述：查看一段时间内TRS和采购采集到文章的微信公众号的交集、TRS独有和采购独有的微信公众号数量和所占比例。
</p>
			
       		
       		 <table class="table">
       		 	<tr>
       		 		<td>TRS</td>
       		 		<td>${map.trsWeCharPublicNumberAlone + map.weChatPublicNumberIntersection}</td>
       		 	</tr>
       		 	<tr>
       		 		<td>采购</td>
       		 		<td>${map.procurementWeChatPublicNumberAlone + map.weChatPublicNumberIntersection}</td>
       		 	</tr>
       		 </table>
       		
       		
		</div>
		<!-- 公众号对比部分结束 -->
		<!-- TRS和采购公众号平均每天发文数量对比开始 -->
		<div id="caigou">
			<!-- 4/12-4/21 TRS和采购微信公众号差异对比 -->
			<div id="main10" style="width:100%; height:500px"></div>
			<p>描述：将TRS和采购当日发布文章的微信公众号数量进行对比，并查看两者之间的变化趋势。</p>
		</div>
		<!-- TRS和采购公众号平均每天发文数量对比结束-->
		</div>

	</div>
<script type="text/javascript" src="${base}/scripts/weixinreport/jquery.min.js"></script>
	
	<script src="/js/bootstrap.min.js"></script>
	<script src="/js/echarts.min.js"></script>
	<script src="/js/navbarscroll.js">	</script>
<script>
window.onload=function(){
	//采购文章数对比
	var datearr = new Array();
	var zicaiarr = new Array();
	var caigouarr = new Array();
	<#list map.main1xAixs as item>
		datearr.push(${item})
	</#list>
	<#list map.zicailist as item>
		zicaiarr.push(${item?c})
	</#list>
	<#list map.caigoulist as item>
		caigouarr.push(${item?c})
	</#list>
	var head1 = ${map.head1};
	// 每日采集文章量对比
    var myChart1 = echarts.init(document.getElementById('main1'));
    // 指定图表的配置项和数据
    var option1 = {
        title: {
            text: head1,
            x:'left'
        },
        tooltip: { 
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器,坐标轴触发有效
            type : 'shadow'        // 默认为直线,可选为：'line' | 'shadow'
            }
        },

        legend: {
            data:['自采','采购'],
            right:'right'
        },
      
        barGap : ' 0%',
        xAxis: [{
            type:'category',

             axisLabel:{
                    interval:0,
                    rotate:45,
                    margin:8,
                    textStyle:{
                        color:"#222"
                     }
                 },
            data: datearr
        }],
        yAxis: {},
        series: [{
            name: '自采',
            type: 'bar',
            
            itemStyle:{normal:{color:'#2EC7C9'}},
            data: zicaiarr
        },{
            name: '采购',
            type: 'bar',

            itemStyle:{normal:{color:'#A08EC3'}},
            data: caigouarr
        }]
    };
    myChart1.setOption(option1);
    // 每天采集文章的重复情况对比
    var main2xAxis = ${map.main2xAxis}
    var weiChatPublicNumberArticalIntersectionToday = ${map.weiChatPublicNumberArticalIntersectionToday}
    var trsWeChatPublicNumberArticalTodayAlone = ${map.trsWeChatPublicNumberArticalTodayAlone}
    var procurementWeChatPublicNumberArticalTodayAlone = ${map.procurementWeChatPublicNumberArticalTodayAlone}
    var myChart2 = echarts.init(document.getElementById('main2'));
    var option2 = {
        title : {
            text : "TRS与采购每日数据异同",
            x:'left'
        },
        tooltip : {
            trigger : 'axis',
            showDelay : 0, // 显示延迟,添加显示延迟可以避免频繁切换,单位ms
            axisPointer : {            // 坐标轴指示器,坐标轴触发有效
                type : 'shadow'        // 默认为直线,可选为：'line'  'shadow'
            }
        },
        legend: {
            data:['交集', 'TRS独有','采购独有'],
            right:'right'
        },

        calculable : true,
        yAxis : [
            {
                type : 'value'
            }
        ],
        xAxis : [
            {
                type : 'category',
                axisLabel:{
                    interval:0,
                    rotate:45,
                    margin:8,
                    textStyle:{
                        color:"#222"
                    }
                },
                data: main2xAxis.split(",")
            },

        ],
        series : [
            {
                name:'交集',
                type:'bar',
                stack: '总量',
                itemStyle : { normal: {color:'#2AB5B6'}},
                barWidth:20,
                // itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                data:weiChatPublicNumberArticalIntersectionToday.split(",")
            },
            {
                name:'TRS独有',
                type:'bar',
                stack: '总量',
                barWidth:20,
                itemStyle : { normal: {color:'#FF6347'}},
                data:trsWeChatPublicNumberArticalTodayAlone.split(",")
            },
            {
                name:'采购独有',
                type:'bar',
                stack: '总量',
                barWidth:20,
                itemStyle : { normal: {color : '#5C93F8'}},
                data:procurementWeChatPublicNumberArticalTodayAlone.split(",")
            }
        ]
    };
    myChart2.setOption(option2);
    // 一段时间内采集数据总量的异同
    var head3 = ${map.head3};
    var weiChatPublicNumberArticalIntersection = ${map.weiChatPublicNumberArticalIntersection};
    var trsWeChatPublicNumberArticalAlone = ${map.trsWeChatPublicNumberArticalAlone};
    var procurementWeChatPublicNumberArticalAlone = ${map.procurementWeChatPublicNumberArticalAlone};
    var myChart3 = echarts.init(document.getElementById('main3'));
    option3 = {
        title : {
            text: head3,
            x:'left'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a}<br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            right: 'right',
            data: ['TRS独有','采购独有','交集']
        },
        color:['#57D2D3', '#B6A2DE','#FFB980'] ,
        series : [
            {
                name: '访问来源',
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {value:weiChatPublicNumberArticalIntersection, name:'交集'},
                    {value:trsWeChatPublicNumberArticalAlone, name:'TRS独有'},
                    {value:procurementWeChatPublicNumberArticalAlone, name:'采购独有'}
                ],
                itemStyle: {
                    normal:{
                        label:{
                            show: true,
                            formatter: '{b} : {c} ({d}%)'
                        },

                        labelLine :{show:true}
                    } ,
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    myChart3.setOption(option3);

    // 微信采集某日发布数据所需天数对比图
    var trs0 = ${map.trs0};var trs1 = ${map.trs1};var trs2 = ${map.trs2};var trs3 = ${map.trs3};
    var trs4 = ${map.trs4};var trs5 = ${map.trs5};var trs6 = ${map.trs6};
    var trs7 = ${map.trs7};var trs8 = ${map.trs8};var trs9 = ${map.trs9};var trs10 = ${map.trs10};
    var procurement0 = ${map.procurement0};var procurement1 = ${map.procurement1};var procurement2 = ${map.procurement2};
    var procurement3 = ${map.procurement3};var procurement4 = ${map.procurement4};var procurement5 = ${map.procurement5};
    var procurement6 = ${map.procurement6};var procurement7 = ${map.procurement7};var procurement8 = ${map.procurement8};
    var procurement9 = ${map.procurement9};var procurement10 = ${map.procurement10};
    var main4xAxis = ${map.main4xAxis};
    var myChart4 = echarts.init(document.getElementById('main4'));
    var option4 = {
        title: {
            text: '微信采集某日发布数据所需天数对比图',
            x:'left'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器,坐标轴触发有效
                type : 'shadow'        // 默认为直线,可选为：'line' | 'shadow'
            }
        },
        legend: {
            data:['当天','1天','2天','3天','4天','5天','6天','7天','8天','9天','10天以上的'],
            right: 'right',
            orient: 'vertical',
        },
        xAxis: [{
            type:'category',
            axisLabel:{
                interval:0,
                rotate:90,
                margin:8,
                textStyle:{
                    color:"#222"
                }
            },
            data: main4xAxis.split(",")
        }],
        yAxis: {},
        series: [
            {
                name: '当天',
                type: 'bar',
                stack:'a',
                itemStyle:{normal:{color:'#4572A7'}},
                data: trs0.split(",")
            },
            {
                name: '1天',
                type: 'bar',
                stack:'a',
                itemStyle:{normal:{color:'#AA4643'}},
                data: trs1.split(",")
            },
            {
                name: '2天',
                type: 'bar',
                stack:'a',
                itemStyle:{normal:{color:'#89A54E'}},
                data: trs2.split(",")
            },
            {
                name: '3天',
                type: 'bar',
                stack:'a',
                itemStyle:{normal:{color:'#D7504B'}},
                data: trs3.split(",")
            },
            {
                name: '4天',
                type: 'bar',
                stack:'a',
                itemStyle:{normal:{color:'#4198AF'}},
                data: trs4.split(",")
            },
            {
                name: '5天',
                type: 'bar',
                stack:'a',
                itemStyle:{normal:{color:'#ffc6b2'}},
                data: trs5.split(",")
            },
            {
                name: '6天',
                type: 'bar',
                stack:'a',
                itemStyle:{normal:{color:'#e8bc8f'}},
                data: trs6.split(",")
            },
            {
                name: '7天',
                type: 'bar',
                stack:'a',
                itemStyle:{normal:{color:'#D19392'}},
                data: trs7.split(",")
            },
            {
                name: '8天',
                type: 'bar',
                stack:'a',
                itemStyle:{normal:{color:'#B9CD96'}},
                data: trs8.split(",")
            },
            {
                name: '9天',
                type: 'bar',
                stack:'a',
                itemStyle:{normal:{color:'#A99BBD'}},
                data: trs9.split(",")
            },
            {
                name: '10天以上的',
                type: 'bar',
                itemStyle:{normal:{color:'#91C3D5'}},
                stack:'a',
                data: trs10.split(",")
            },
            {
                name: '当天',
                type: 'bar',
                stack:'b',
                itemStyle:{normal:{color:'#4572A7'}},
                data: procurement0.split(",")
            },
            {
                name: '1天',
                type: 'bar',
                stack:'b',
                itemStyle:{normal:{color:'#AA4643'}},
                data: procurement1.split(",")
            },
            {
                name: '2天',
                type: 'bar',
                stack:'b',
                itemStyle:{normal:{color:'#89A54E'}},
                data: procurement2.split(",")
            },
            {
                name: '3天',
                type: 'bar',
                stack:'b',
                itemStyle:{normal:{color:'#D7504B'}},
                data: procurement3.split(",")
            },
            {
                name: '4天',
                type: 'bar',
                stack:'b',
                itemStyle:{normal:{color:'#4198AF'}},
                data: procurement4.split(",")
            },
            {
                name: '5天',
                type: 'bar',
                stack:'b',
                itemStyle:{normal:{color:'#ffc6b2'}},
                data: procurement5.split(",")
            },
            {
                name: '6天',
                type: 'bar',
                stack:'b',
                itemStyle:{normal:{color:'#e8bc8f'}},
                data: procurement6.split(",")
            },
            {
                name: '7天',
                type: 'bar',
                stack:'b',
                itemStyle:{normal:{color:'#D19392'}},
                data: procurement7.split(",")
            },
            {
                name: '8天',
                type: 'bar',
                stack:'b',
                itemStyle:{normal:{color:'#B9CD96'}},
                data: procurement8.split(",")
            },
            {
                name: '9天',
                type: 'bar',
                stack:'b',
                itemStyle:{normal:{color:'#A99BBD'}},
                data: procurement9.split(",")
            },
            {
                name: '10天以上的',
                type: 'bar',
                itemStyle:{normal:{color:'#91C3D5'}},
                stack:'b',
                data: procurement10.split(",")
            }]
    };
    myChart4.setOption(option4);

    //todo每日发布数据从发布到采集的时间
    //...


    //每日采集数据当天和历史文章量对比图
    var main5xAixs = ${map.main5xAixs};
    var procurementHistory = ${map.procurementHistory};
    var procurementToday = ${map.procurementToday};
    var trsHistory = ${map.trsHistory};
    var trsToday = ${map.trsToday};
    // 每日采集数据当天和历史文章量对比图
    var myChart5 = echarts.init(document.getElementById('main5'));
    var option5 = {
        title : {
            text : "每日采集数据当天和历史文章量对比图",
            x:'left'
        },
        tooltip : {
            trigger : 'axis',
            showDelay : 0, // 显示延迟,添加显示延迟可以避免频繁切换,单位ms
            axisPointer : {            // 坐标轴指示器,坐标轴触发有效
                type : 'shadow'        // 默认为直线,可选为：'line'  'shadow'
            }
        },
        legend: {
            data:['TRS采集历史数据','TRS采集当日数据','采购采集历史数据', '采购采集当日数据',],
            right:'right'
        },

        calculable : true,
        yAxis : [
            {
                type : 'value'
            }
        ],
        xAxis : [
            {
                type : 'category',
                axisLabel:{
                    interval:0,
                    rotate:90,
                    margin:8,
                    textStyle:{
                        color:"#222"
                    }
                },
                data: main5xAixs.split(",")
            }

        ],
        series : [
            {
                name: 'TRS采集当日数据',
                type:'bar',
                stack: 'a',
                itemStyle : { normal: {color:'#8CD88C'}},
                data:trsToday.split(",")
            },
            {
                name:'TRS采集历史数据',
                type:'bar',
                stack: 'a',
                itemStyle : { normal: {color:'#FFC6B2'}},
                // itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                data:trsHistory.split(",")
            },
            {
                name:'采购采集当日数据',
                type:'bar',
                stack: 'b',
                itemStyle : { normal: {color : '#A07BBC'}},
                data:procurementToday.split(",")
            },
            {
                name:'采购采集历史数据',
                type:'bar',
                stack: 'b',
                itemStyle : { normal: {color : '#93A9cF'}},
                data:procurementHistory.split(",")
            }
        ]
    }
    myChart5.setOption(option5);
    //每日采集文章的微信公众号数对比
    var main6xAxis = ${map.main6xAxis};
    var procurementWeChatPublicArticalNumber = ${map.procurementWeChatPublicArticalNumber};
    var trsWeChatPublicArticalNumber = ${map.trsWeChatPublicArticalNumber};
    // 每日采集文章的微信公众号数对比
    var myChart6 = echarts.init(document.getElementById('main6'));
    // 指定图表的配置项和数据
    var option6 = {
        title: {
            text: '每日采集文章的微信公众号数对比',
            x:'left'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器,坐标轴触发有效
                type : 'shadow'        // 默认为直线,可选为：'line' | 'shadow'
            }
        },

        legend: {
            data:['TRS微信公众号','采购微信公众号'],
            right:'right'
        },

        barGap : ' 0%',
        xAxis: [{
            type:'category',

            axisLabel:{
                interval:0,
                rotate:45,
                margin:8,
                textStyle:{
                    color:"#222"
                }
            },
            data: main6xAxis.split(",")
        }],
        yAxis: {},
        series: [{
            name: 'TRS微信公众号',
            type: 'bar',

            itemStyle:{normal:{color:'#7978F0'}},
            data: trsWeChatPublicArticalNumber.split(",")

        },{
            name: '采购微信公众号',
            type: 'bar',

            itemStyle:{normal:{color:'#2EC7C9'}},
            data: procurementWeChatPublicArticalNumber.split(",")
        }]
    };
    myChart6.setOption(option6);
    //TRS和采购每天采集的微信公众号数量对比
    var head7 = ${map.head7};
    var main7xAxis = ${map.main7xAxis};
    var procurementWeChatPublicNumber = ${map.procurementWeChatPublicNumber};
    var trsWeChatPublicNumber = ${map.trsWeChatPublicNumber};
    var myChart7 = echarts.init(document.getElementById('main7'));
    // 指定图表的配置项和数据
    var option7 = {
        title: {
            text: head7,
            x:'left'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器,坐标轴触发有效
                type : 'shadow'        // 默认为直线,可选为：'line' | 'shadow'
            }
        },

        legend: {
            data:['TRS','采购'],
            right:'right'
        },

        barGap : ' 0%',
        xAxis: [{
            type:'category',

            axisLabel:{
                interval:0,

                margin:8,
                textStyle:{
                    color:"#222"
                }
            },
            data: main7xAxis.split(",")
        }],
        yAxis: {},
        series: [{
            name: 'TRS',
            type: 'bar',
            itemStyle:{normal:{color:'#FCDC57'}},
            data: trsWeChatPublicNumber.split(",")
        },{
            name: '采购',
            type: 'bar',
            itemStyle:{normal:{color:'#72BBD7'}},
            data: procurementWeChatPublicNumber.split(",")

        }]
    };
    myChart7.setOption(option7);

    //TRS和采购微信公众号差异对比
    var head8 = ${map.head8};
    var main8xAxis = ${map.main8xAxis};
    var procurementWeChatPublicNumberTodayAlone = ${map.procurementWeChatPublicNumberTodayAlone};
    var trsWeChatPublicNumberTodayAlone = ${map.trsWeChatPublicNumberTodayAlone};
    var weiChatPublicNumberIntersectionToday = ${map.weiChatPublicNumberIntersectionToday};
    var myChart8 = echarts.init(document.getElementById('main8'));
    // 指定图表的配置项和数据
    var option8 = {
        title: {
            text: head8,
            x:'left'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器,坐标轴触发有效
                type : 'shadow'        // 默认为直线,可选为：'line' | 'shadow'
            }
        },

        legend: {
            data:['TRS独有','采购独有','交集'],
            right:'right'
        },

        barGap : ' 0%',
        xAxis: [{
            type:'category',

            axisLabel:{
                interval:0,

                margin:8,
                textStyle:{
                    color:"#222"
                }
            },
            data: main8xAxis.split(",")
        }],
        yAxis: {},
        series: [{
            name: '交集',
            type: 'bar',
            stack: 'b',
            barWidth:20,
            itemStyle:{normal:{color:'#E6E225'}},
            data: weiChatPublicNumberIntersectionToday.split(",")


        },{
            name: 'TRS独有',
            type: 'bar',
            stack: 'b',
            barWidth:20,
            itemStyle:{normal:{color:'#A6E357'}},
            data: trsWeChatPublicNumberTodayAlone.split(",")

        },{
            name: '采购独有',
            type: 'bar',
            stack: 'b',
            barWidth:20,
            itemStyle:{normal:{color:'#428BCA'}},
            data: procurementWeChatPublicNumberTodayAlone.split(",")
        }]
    };
    myChart8.setOption(option8);

    //一段时间内TRS与采购的微信公众号异同
    var head9 = ${map.head9};
    var trsWeCharPublicNumberAlone = ${map.trsWeCharPublicNumberAlone};
    var procurementWeChatPublicNumberAlone = ${map.procurementWeChatPublicNumberAlone};
    var weChatPublicNumberIntersection = ${map.weChatPublicNumberIntersection};
    var myChart9 = echarts.init(document.getElementById('main9'));
    option9 = {
        title : {
            text: head9,
            x:'left'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a}<br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            right: 'right',
            data: ['TRS独有','采购独有','交集']
        },
        color:['#57D2D3', '#B6A2DE','#FFB980'] ,
        series : [
            {
                name: '访问来源',
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {value:trsWeCharPublicNumberAlone, name:'TRS独有'},
                    {value:procurementWeChatPublicNumberAlone, name:'采购独有'},
                    {value:weChatPublicNumberIntersection, name:'交集'}
                ],
                itemStyle: {
                    normal:{
                        label:{
                            show: true,
                            formatter: '{b} : {c} ({d}%)'
                        },
                        labelLine :{show:true}
                    } ,
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    myChart9.setOption(option9);
    //采集到的微信公众号平均每天发文数量
    var head10 = ${map.head10};
    var main10xAxis = ${map.main10xAxis};
    var trsArticalAverage = ${map.trsArticalAverage};
    var procurementArticalAverage = ${map.procurementArticalAverage};
    var myChart10 = echarts.init(document.getElementById('main10'));
    // 指定图表的配置项和数据
    var option10 = {
        title: {
            text: head10,
            x:'left'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器,坐标轴触发有效
                type : 'shadow'        // 默认为直线,可选为：'line' | 'shadow'
            }
        },

        legend: {
            data:['TRS','采购'],
            right:'right'
        },

        barGap : ' 0%',
        xAxis: [{
            type:'category',

            axisLabel:{
                interval:0,

                margin:8,
                textStyle:{
                    color:"#222"
                }
            },
            data: main10xAxis.split(",")
        }],
        yAxis: {},
        series: [{
            name: 'TRS',
            type: 'bar',
            itemStyle:{normal:{color:'#03A9F4'}},
            data: trsArticalAverage.split(",")
        },{
            name: '采购',
            type: 'bar',
            itemStyle:{normal:{color:'#FFEB3B'}},
            data: procurementArticalAverage.split(",")

        }]
    };
    myChart10.setOption(option10);
}
</script>
	
</body>
</html>