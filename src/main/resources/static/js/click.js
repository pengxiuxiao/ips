layui.use(['element', 'table', 'laydate', 'jquery','upload'], function(){
    var element = layui.element,
        laydate = layui.laydate,
        layer = layui.layer,
        form = layui.form,
        table = layui.table;

    var messageType = '1';
    var user_id = localStorage.getItem('user_id');
    if (user_id == null || user_id == '') {
        window.location.href = 'login.html';
    }
    //首次渲染列表

    var startDate = '', endDate = '';

    //获取列表接口
    var getOrderList = function (url) {
        table.render({
            elem: '#course'
            ,url: url
            // ,url: 'js/data.json'
            ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            ,limit: 90
            ,cols: [[
                {type:'checkbox', fixed: 'left'}
                ,{field:'id', title: 'id', width: 80}
                ,{field:'courseName', title: '课程名', width: 260, sort: true}
                ,{field:'studentName', title: '学员姓名', sort: true}
                ,{field:'cardNumber', title: '卡号', width: 120, sort: true}
                ,{field:'cType', title: '签到区间', width: 260, sort: true}
                ,{field:'clickTime', title: '打卡时间', templet: '#clickTime', width: 180, sort: true}
            ]]
            ,page: true
        });

    };
    var url = global + '/click/list' + '?user_id='+ user_id;
    getOrderList(url);

    //搜索
    $(".doSearch").click(function () {
        var content = $(".keyWord").val();
        url = global + '/click/list' + '?user_id='+ user_id + '&key=' + content;
        getOrderList(url);
    })


    //发布消息
    $(document).on('click', '.add-course-btn', function () {
        //填充数据
        // $(".layui-input.nId").val('');
        $(".layui-input.title").val('');
        $(".layui-input.content").val('');
        //获取教室列表
        getRoom();
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: '516px',
            content: $('.course-content')
        });
    });
    //提交添加数据文件
    $(".addCourseBtn").click(function(){
        //data.field是提交数据
        var dataj = new FormData($("#addCourseForm")[0]);
        dataj.append('user_id', user_id);
        dataj.append('room_id', $(".layui-input.room_id").val());
        $.ajax({
            type:'post',
            url: uploadCourse,
            data:dataj,
            dataType:'json',
            processData: false,
            contentType: false,
            success:function (res) {
                if(res.code == 0){//
                    layer.msg('推送成功');
                    $(".layui-laypage-btn").click();
                }else {
                    layer.msg(res.msg);
                }
            },
            error:function (err) {
                layer.msg('操作失败，请稍后重试！', 'error');
            }
        })
        layer.closeAll();
        return false;
    });

    //监听添加提交按钮
    form.on('submit(editbtn)', function(indata){
        //添加、编辑学生信息post请求
        var postData = {name:indata.field.name, course_id:indata.field.nId,
            start_time:indata.field.start_time, end_time:indata.field.end_time,
            user_id:user_id, word_size: indata.field.word_size};
        //ajax调用后台添加接口
        $.ajax({
            type:'post',
            async: false,
            url:editCourse,
            data:postData,
            dataType:'json',
            success:function (res) {
                if(res.code == 0){//0
                    layer.closeAll();
                    $(".layui-laypage-btn").click();
                }else {
                    layer.msg('操作失败，请稍后重试！', 'error');
                }
            },
            error:function (err) {
                layer.msg('操作失败，请稍后重试！', 'error');
            }
        })
    });


    //点击操作
    table.on('tool(order-table)', function(obj){
        if(obj.event === 'edit'){//编辑
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0,
                area: '516px',
                shadeClose: true,
                content: $('.edit-course-content')
            });
            $(".layui-input.nId").val(obj.data.id);
            $(".layui-input.name").val(obj.data.cName);
            $(".layui-input.start_time").val(obj.data.cStartTime);
            $(".layui-input.end_time").val(obj.data.cEndTime);
            $(".layui-input.word_size").val(obj.data.cWordSize);
            //日期时间范围
            laydate.render({
                elem: '#start_time'
                ,value: new Date(obj.data.cStartTime).format('yyyy-MM-dd hh:mm:ss')
                ,type: 'datetime'
            });
            //日期时间范围
            laydate.render({
                elem: '#end_time'
                ,value: new Date(obj.data.cEndTime).format('yyyy-MM-dd hh:mm:ss')
                ,type: 'datetime'
            });

        }else if(obj.event === 'del'){//删除
            layer.confirm('确认删除 ' + obj.data.cName + '？', function(index){
                $.ajax({
                    type:'post',
                    url: deleteCourse,
                    data:{course_id:obj.data.id, user_id:user_id},
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
        content: function(value){
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

    //取消按钮
    $(".cancel-btn").click(function () {
        layer.closeAll();
    })


    //导出
    $(".out-click-btn").click(function () {
        var content = $(".keyWord").val();
        window.location.href = global + "/click/export?user_id=" + user_id + '&key=' + content;
    })

//    切换消息类型，刷新数据
    form.on('select(msg-type)', function(data){
        messageType = data.value;
        var content = $(".keyWord").val();
        url = global + '/notice/list' + '?user_id='+ user_id + '&type=' + messageType + '&key=' + content;
        getOrderList(url);
    });

    //上传excel
    $("#upload-excel").change(function () {
        var self = this
        if(self.files && self.files[0]){
            var fileName = self.files[0].name;
            var index = fileName.lastIndexOf('.');
            var fileType = fileName.substring(index + 1, fileName.length);
            console.log(fileType);
            if (fileType === 'xls' || fileType === 'xlsx'){
            }else{
                $('#upload-PPT').val('');
                layer.msg('请选择EXCEL文件', function(){
                    //关闭后的操作
                });
            }
        }
    })


    //批量删除
    $('.bdcourse-btn').on('click', function(){
        var checkStatus = table.checkStatus('course');
        var data = JSON.stringify(checkStatus.data);
        if (data == "" || data == "[]") {
            layer.msg('请先选择Pad！');
            return;
        }
        layer.confirm('确认批量删除培训? ', function(index){
            //ajax调用后台添加接口
            $.ajax({
                type:'post',
                url: global + '/course/bdc',
                data:{idList:data, user_id:user_id},
                dataType:'json',
                success:function (res) {
                    if(res.code == 0){//0
                        layer.msg('操作成功');
                        $(".layui-laypage-btn").click();
                    }else {
                        layer.msg('操作失败');
                    }
                },
                error:function (err) {
                    layer.msg('操作失败');
                }
            })
        })
    });


    //查询学校所有班级
    function getRoom() {
        $.ajax({
            url:roomList,
            type:"post",
            data:{"user_id":localStorage.getItem("user_id"),page:1,limit:100},
            dataType:"json",
            success:function (data) {
                var data = data.data;
                var roomhtml = '<option value="0" class="tdstyle texts">'+"请选择教室"+'</option>';
                for (var i = 0; i < data.length; i++){
                    roomhtml += '<option value="'+data[i].id+'" class="tdstyle texts">'+data[i].rName+'</option>';
                }
                $("#room_id").html(roomhtml);
                form.render();
            }
        })
    }

});