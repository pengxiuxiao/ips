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
        if(obj.event === 'edit'){//编辑
            layer.load(2);
            $.ajax({
                type:'post',
                url: monitorPad,
                data:{id:obj.data.id, user_id:user_id},
                dataType:'json',
                success:function (res) {
                    layer.closeAll('loading');
                    if(res.code == 0){//
                        layer.open({
                            type: 1,
                            title: false,
                            closeBtn: 0,
                            area: '916px',
                            shadeClose: true,
                            content: $('.monitor-content')
                        });
                        $(".layui-input.nId").val(obj.data.id);
                        $(".layui-input.module").val(obj.data.pModule);
                        $("#moImg").attr("src",res.data.url)
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

});