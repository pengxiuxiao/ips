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
    if (user_id == "1007") {
        $(".index").css("display","block");
        $(".course").css("display","block");
        $(".room").css("display","block");
        $(".setting").css("display","block");
        $(".pad").css("display","block");
        $(".app").css("display","block");
        $(".dining").css("display","block");
        $(".card").css("display","block");
    }else{
        $(".dining").css("display","block");
        $(".card").css("display","block");
    }

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
                ,{field:'id', title: 'id', width: 80, sort: true}
                ,{field:'cName', title: '培训名', sort: true}
                ,{field:'cTypeDes', title: '培训类别', sort: true}
                // ,{field:'cWordSize', title: '文字大小'}
                ,{field:'cRoomName', title: '所在教室', sort: true}
                ,{field:'zaoTime', title: '上午区间', templet: '#zaoTime'} //width 支持：数字、百分比和不填写。你还可以通过 minWidth 参数局部定义当前单元格的最小宽度，layui 2.2.1 新增
                ,{field:'wuTime', title: '下午区间', templet: '#wuTime'}
                ,{field:'wanTime', title: '晚上区间', templet: '#wanTime'}
                ,{field:'', title: '操作', templet: '#barDemo', unresize: true, align: 'center', width: 200}
            ]]
            ,page: true
        });

    };
    var url = global + '/dining/list' + '?user_id='+ user_id;
    getOrderList(url);

    //搜索
    $(".doSearch").click(function () {
        var content = $(".keyWord").val();
        url = global + '/dining/list' + '?user_id='+ user_id + '&key=' + content;
        getOrderList(url);
    })


    //发布消息
    $(document).on('click', '.add-course-btn', function () {
        //填充数据
        // $(".layui-input.nId").val('');
        $(".layui-input.title").val('');
        $(".layui-input.content").val('');
        $(".layui-input.zao_time").val('07:30:00 - 08:30:00');
        $(".layui-input.wu_time").val('12:00:00 - 13:00:00');
        $(".layui-input.wan_time").val('17:30:00 - 18:30:00');

        //日期时间选择器
        laydate.render({
            elem: '#zao_time'
            ,type: 'time'
            ,range: true
        });
        //日期时间选择器
        laydate.render({
            elem: '#wu_time'
            ,type: 'time'
            ,range: true
        });
        //日期时间选择器
        laydate.render({
            elem: '#wan_time'
            ,type: 'time'
            ,range: true
        });
        //获取教室列表
        getRoom();
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: '516px',
            shadeClose: true,
            content: $('.course-content')
        });
    });
    //提交添加数据文件
    $(".addCourseBtn").click(function(){
        //data.field是提交数据
        var dataj = new FormData($("#addCourseForm")[0]);
        var nid = $(".layui-input.nId").val();
        dataj.append('user_id', user_id);
        dataj.append('nId', nid);
        dataj.append('room_id', $(".layui-input.room_id").val());
        dataj.append('zao_time', $(".layui-input.zao_time").val());
        dataj.append('wu_time', $(".layui-input.wu_time").val());
        dataj.append('wan_time', $(".layui-input.wan_time").val());

        var postUrl;
        if (nid != null && nid != "") {
            postUrl = global + '/dining/edit';
        } else {
            postUrl = global + '/dining/add';
        }
        $.ajax({
            type:'post',
            url: postUrl,
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
                    $(".layui-laypage-btn").click();
                }
            },
            error:function (err) {
                layer.msg('操作失败，请稍后重试！', 'error');
            }
        });
        layer.closeAll();
        return false;
    });


    //点击操作
    table.on('tool(order-table)', function(obj){
        if(obj.event === 'edit'){//编辑
            $.ajax({
                url:roomList,
                type:"post",
                data:{"user_id":localStorage.getItem("user_id"),page:1,limit:100},
                dataType:"json",
                success:function (res) {
                    if(res.code == 0){//
                        setRoomSelect(res.data,obj.data.cRoomId);
                    }else {
                        layer.msg('操作失败！');
                    }
                    layer.open({
                        type: 1,
                        title: false,
                        closeBtn: 0,
                        area: '516px',
                        shadeClose: true,
                        content: $('.course-content')
                    });
                    $(".layui-input.nId").val(obj.data.id);
                    $(".layui-input.name").val(obj.data.cName);
                    $(".layui-input.zao_time").val(obj.data.zaoTime);
                    $(".layui-input.wu_time").val(obj.data.wuTime);
                    $(".layui-input.wan_time").val(obj.data.wanTime);
                    setcTypeSelect(obj.data.cType);
                },
                error:function (err) {
                    layer.msg('操作失败，请稍后重试！', 'error');
                }

            })
        }else if(obj.event === 'del'){//删除
            layer.confirm('确认删除 ' + obj.data.cName + '？', function(index){
                $.ajax({
                    type:'post',
                    url: global + '/dining/delete',
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
        }else if(obj.event === 'cCard'){//打卡记录
            window.location.href="click.html?user_id=" + obj.data.uId + "&course=" +  obj.data.id;
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



//    切换消息类型，刷新数据
    form.on('select(msg-type)', function(data){
        messageType = data.value;
        var content = $(".keyWord").val();
        url = global + '/notice/list' + '?user_id='+ user_id + '&type=' + messageType + '&key=' + content;
        getOrderList(url);
    });

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
                url: global + '/dining/bdc',
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

    //初始化教室下拉菜单
    function setRoomSelect(rooms,rid){
        var roomshtml;
        if(rid == ''){
            roomshtml = '<option value="0" selected >'+"请选择教室"+'</option>';
        }else{
            roomshtml = '<option value="0" >'+"请选择教室"+'</option>';
        }
        for (var i = 0; i < rooms.length; i++){
            if(rid == rooms[i].id){
                roomshtml += '<option value="'+ rooms[i].id + '"selected >'+ rooms[i].rName + '</option>';
            }else{
                roomshtml += '<option value="'+ rooms[i].id + '">'+ rooms[i].rName + '</option>';
            }
        }
        $("#room_id").html(roomshtml);
        form.render();
    };

    //初始化培训类型下拉菜单
    function setcTypeSelect(type){
        var roomshtml;
        if (type == "1") {
            roomshtml = '<option value="1" selected>餐厅打卡</option>';
        } else {
            roomshtml = '<option value="2" selected>上课签到</option>'
        }
        $("#course_type").html(roomshtml);
        form.render();
    };
});