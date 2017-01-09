$(document).ready(function(e) {        
			
				//滚动监听
				$(window).scroll(function(event) {
					if($(window).scrollTop()>$('#banner').outerHeight(true)){

						$('#navbar-example').addClass('navbar-fixed-top');
						$('.zhanwei').css('display','block');
					}else{

						// $('#navbar-example').removeClass('navbar-fixed-top');
						$('.zhanwei').css('display','none');
					}


					var gdtTop = $(window).scrollTop();
					var articleH=$('#article').offset().top;
					var commonH=$('#common').offset().top;
					var caigouH=$('#caigou').offset().top;

					

					if(gdtTop>=0&&gdtTop<commonH-50){
						$('#myCollapse li').eq(0).addClass('active');
					}else{
						$('#myCollapse li').eq(0).removeClass('active');
					}
					
					if(gdtTop>=commonH-50&&gdtTop<caigouH-50){
						
						$('#myCollapse li').eq(1).addClass('active');
					}else{
						$('#myCollapse li').eq(1).removeClass('active');
					}
					if(gdtTop>=caigouH-50){
						$('#myCollapse li').eq(2).addClass('active');
					}else{
						$('#myCollapse li').eq(2).removeClass('active');
					}
					
					
				});
				

				// 导航条点击高亮
				$('#myCollapse li a').click(function(){
					// for(var i = 0; i < $('#myCollapse li').length; i++){
					// 	$('#myCollapse li').eq(i).removeClass('active')
					// }
					$(this).parent().addClass('active');
					var partH = $(this.hash).offset().top;
					$('html,body').animate({scrollTop:partH -50},1000);

						return false;

				})
			});	