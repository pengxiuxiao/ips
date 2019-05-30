layui.use(['element', 'table', 'laydate', 'jquery','upload'], function(){
    var element = layui.element,
        laydate = layui.laydate,
        layer = layui.layer,
        form = layui.form,
        $ = layui.$,
        table = layui.table;

    var user_id = localStorage.getItem('user_id');
    if (user_id == null || user_id == '') {
        window.location.href = 'login.html';
    }
    //首次渲染列表

    var startDate = '', endDate = '';

    //获取列表接口
    var getOrderList = function (url) {
    	
        table.render({
            elem: '#test'
            ,url: url
            // ,url: 'js/data.json'
            ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            ,limit: 10
            ,cols: [[
                {field:'id', title: 'id', width: 80}
                ,{field:'code', title: 'code标识'}
                ,{field:'roomName', title: '所属教室'}
                // ,{field:'pLocation', title: '安装位置'} //width 支持：数字、百分比和不填写。你还可以通过 minWidth 参数局部定义当前单元格的最小宽度，layui 2.2.1 新增
                ,{field:'pStatus', title: '在线状态'}
                ,{field:'isBlack', title: '是否黑屏'}
                ,{field:'updateTime', title: '请求时间', templet: '#createTime'}
                ,{field:'', title: '操作', templet: '#barDemo', unresize: true, align: 'center', width: 150}
            ]]
        ,page: true
        });
    };
    var url = global + '/pad/list' + '?user_id='+ user_id;
    getOrderList(url);

    //搜索
    $(".doSearch").click(function () {
        var content = $(".keyWord").val();
        url = global + '/pad/list' + '?user_id='+ user_id + '&key=' + content;
        getOrderList(url);
    })


    //点击添加
    $(document).on('click', '.add-refresh-btn', function () {
        $(".layui-laypage-btn").click();
    })

    //点击编辑 (具体类名自己替换)
    table.on('tool(order-form)', function(obj){

        // layer.load(2);
        // layer.closeAll('loading');
        if(obj.event === 'edit'){//编辑
            layer.load(2);
            $.ajax({
                type:'post',
                url: monitorPad,
                data:{code:obj.data.code, user_id:user_id},
                dataType:'json',
                success:function (res) {
                    $("#moImg").attr("src","")
                    layer.closeAll('loading');
                    if(res.code == 0){
                        imgShow("#outerdiv", "#innerdiv", "#bigimg", res.data.url);
                    }else {
                        layer.msg('操作失败！');
                    }
                },
                error:function (err) {
                    layer.msg('操作失败！');
                }
            })
        }else if(obj.event === 'del'){//删除
            layer.confirm('确认切换黑屏 ' + obj.data.roomName + '？', function(index){
                $.ajax({
                    type:'post',
                    url: closePad,
                    data:{id:obj.data.id, user_id:user_id},
                    dataType:'json',
                    success:function (res) {
                        if(res.code == 0){//
                            layer.msg('操作完成！');
                            $(".layui-laypage-btn").click();
                        }else {
                            layer.msg('操作失败！');
                        }
                    },
                    error:function (err) {
                        layer.msg('操作失败！');
                    }
                })
            })
        }
    })

    //自定义验证规则
    form.verify({

        title: function(value){
            if(value == '' || value == null){
                return '请输入标题';
            }
        },
        textareacontent: function(value){
            if(value == '' || value == null){
                return '请输入内容';
            }
        },
        room_list: function(value){
            if(value == '' || value == null){
                return '请选择教室列表';
            }
        }

    });

    //监听添加提交按钮
    form.on('submit(addbtn)', function(indata){
        //data.field是提交数据
        console.log(indata.field);
        var dataj = indata.field;
        var imgFile;
        //获取文件
        // var srcFile = $("#imgForm").find("input")[1].files[0];
        var srcFile;
        if(srcFile != undefined){
            //创建读取文件的对象
            var reader = new FileReader();
            //创建文件读取相关的变量
            reader.readAsDataURL(srcFile);
            reader.onload = function(e){ // reader onload start
                postStudentEdit(indata,imgFile);
            }
        }else{
            postStudentEdit(indata,imgFile);
        }

        return false;
    });

    //取消按钮
    $(".cancel-btn").click(function () {
        layer.closeAll();
    })


	//上传文件
	var $ = layui.jquery,upload = layui.upload;
	//指定允许上传的文件类型
	upload.render({
		elem: '#test3'
		,url: global + '/user/sendKFNotice'
		,accept: 'file' //普通文件
		,exts: 'xls|xlsx|csv' //只允许上传压缩文件
		,size: 1000 //限制文件大小，单位 KB
		,before: function(res){ 
		this.data={'usreId':id};
		}
		,done: function(res){
			//上传结果
			if(res.status == 0){//0
				layer.msg(res.message);
			}else {
				layer.msg(res.message);
			}
		}		
	});


    //添加、编辑学生信息post请求
    function postStudentEdit(indata, imgFile) {
        var notice_id = indata.field.nId;
        var textareacontent = $(".textareacontent").val();
        var postData;
        var postUrl;
        if(notice_id == ""){
            postUrl = addWord;
            postData = {title:indata.field.title, content:textareacontent, room_list:indata.field.room_list,
                user_id:user_id};
        }else{
            postUrl = editWord;
            postData = {notice_id:notice_id, title:indata.field.title, content:textareacontent, room_list:indata.field.room_list,
                user_id:user_id};
        }
        //ajax调用后台添加接口
        $.ajax({
            type:'post',
            async: false,
            url:postUrl,
            data:postData,
            dataType:'json',
            success:function (res) {
                if(res.code == 0){//0
                    setTimeout(function () {
                        layer.msg('操作完成...', {
                            icon: 16,
                            shade: 0.3,
                            time:false, //取消自动关闭
                            area: ['auto'],
                        });
                        document.location.reload();
                    },200);
                }else {
                    $.toptip(res.msg, 'error');
                }
            },
            error:function (err) {
                $.hideLoading();
                $.toptip('请求失败，请稍后重试！', 'error');
            }
        })
    }

    //展示弹出图 自动缩放
    function imgShow(outerdiv, innerdiv, bigimg, imgsrc){
        $(bigimg).attr("src", imgsrc);//设置#bigimg元素的src属性
        /*获取当前点击图片的真实大小，并显示弹出层及大图*/
        $("<img/>").attr("src", imgsrc).load(function(){
            var windowW = $(window).width();//获取当前窗口宽度
            var windowH = $(window).height();//获取当前窗口高度
            var realWidth = this.width;//获取图片真实宽度
            var realHeight = this.height;//获取图片真实高度
            var imgWidth, imgHeight;
            var scale = 0.8;//缩放尺寸，当图片真实宽度和高度大于窗口宽度和高度时进行缩放
            if(realHeight>windowH*scale) {//判断图片高度
                imgHeight = windowH*scale;//如大于窗口高度，图片高度进行缩放
                imgWidth = imgHeight/realHeight*realWidth;//等比例缩放宽度
                if(imgWidth>windowW*scale) {//如宽度扔大于窗口宽度
                    imgWidth = windowW*scale;//再对宽度进行缩放
                }
            } else if(realWidth>windowW*scale) {//如图片高度合适，判断图片宽度
                imgWidth = windowW*scale;//如大于窗口宽度，图片宽度进行缩放
                imgHeight = imgWidth/realWidth*realHeight;//等比例缩放高度
            } else {//如果图片真实高度和宽度都符合要求，高宽不变
                imgWidth = realWidth;
                imgHeight = realHeight;
            }

            $(bigimg).css("width",imgWidth);//以最终的宽度对图片缩放
            var w = (windowW-imgWidth)/2;//计算图片与窗口左边距
            var h = (windowH-imgHeight)/2;//计算图片与窗口上边距
            $(innerdiv).css({"top":h, "left":w});//设置#innerdiv的top和left属性
            $(outerdiv).fadeIn("fast");//淡入显示#outerdiv及.pimg

        });

        $(outerdiv).click(function(){//再次点击淡出消失弹出层
            $(this).fadeOut("fast");
        });

    }
});