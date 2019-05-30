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


    //获取列表接口
    var getOrderList = function (url) {
        $.ajax({
            url:global + '/pad/setting' + '?code='+ user_id,
            type:"post",
            data:{"code":user_id},
            dataType:"json",
            success:function (data) {
                var data = data.data;
                setModuleSelect(data);
            }
        })

        table.render({
            elem: '#rooms'
            ,url: url
            // ,url: 'js/data.json'
            ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            ,limit: 20
            ,cols: [[
                {type:'checkbox', fixed: 'left'}
                ,{field:'id', title: 'id', width: 80}
                ,{field:'rName', title: '教室名'}
                ,{field:'rLocation', title: '教室位置'}
                ,{field:'rIp', title: '教室Code'} //width 支持：数字、百分比和不填写。你还可以通过 minWidth 参数局部定义当前单元格的最小宽度，layui 2.2.1 新增
                // ,{field:'updateTime', title: '操作时间', templet: '#createTime'}
                ,{field:'', title: '操作', templet: '#barDemo', unresize: true, align: 'center', width: 150}
            ]]
        ,page: true
        });
    };
    var url = global + '/room/list' + '?user_id='+ user_id;
    getOrderList(url);

    //搜索
    $(".doSearch").click(function () {
        var content = $(".keyWord").val();
        url = global + '/room/list' + '?user_id='+ user_id + '&key=' + content;
        getOrderList(url);
    })


    //点击添加
    $(document).on('click', '.add-room-btn', function () {
        //先清表单数据
        $(".modal-content form input").each(function(){
            $(this).val('');
        });
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: '516px',
            shadeClose: true,
            content: $('.modal-content')
        });
    })

    //点击编辑 (具体类名自己替换)
    table.on('tool(order-form)', function(obj){
        if(obj.event === 'edit'){//编辑
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0,
                area: '516px',
                shadeClose: true,
                content: $('.modal-content')
            });
            $(".layui-input.nId").val(obj.data.id);
            $(".layui-input.name").val(obj.data.rName);
            $(".layui-input.location").val(obj.data.rLocation);
            $(".layui-input.ip").val(obj.data.rIp);
        }else if(obj.event === 'del'){//删除
            layer.confirm('确认删除 ' + obj.data.rName + '？', function(index){
                $.ajax({
                    type:'post',
                    url: deleteRoom,
                    data:{room_id:obj.data.id, user_id:user_id},
                    dataType:'json',
                    success:function (res) {
                        if(res.code == 0){//
                            layer.msg('操作完成！');
                            layer.closeAll();
                            $(".layui-laypage-btn").click();
                        }else {
                            layer.msg('操作失败！');
                        }
                    },
                    error:function (err) {
                        $.toptip('操作失败，请稍后重试！', 'error');
                    }
                })
            })
        }
    })

    //自定义验证规则
    form.verify({

        name: function(value){
            if(value == '' || value == null){
                return '请输入教室名';
            }
        },
        location: function(value){
            if(value == '' || value == null){
                return '请输入教室位置';
            }
        },
        ip: function(value){
            if(value == '' || value == null){
                return '请输入教室ip';
            }
        }

    });

    //批量删除
    $('.bdroom-btn').on('click', function(){
        layer.confirm('确认批量删除教室? ', function(index){
            var checkStatus = table.checkStatus('rooms')
                ,data = checkStatus.data;
            // layer.alert(JSON.stringify(data));
            //ajax调用后台添加接口
            $.ajax({
                type:'post',
                url: global + '/room/bdr',
                data:{idList:JSON.stringify(data), user_id:user_id},
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

    //监听添加提交按钮
    form.on('submit(addbtn)', function(indata){
        //data.field是提交数据
        console.log(indata.field);
        postRoomEdit(indata);
        return false;
    });

    //取消按钮
    $(".cancel-btn").click(function () {
        layer.closeAll();
    })


    //添加、编辑学生信息post请求
    function postRoomEdit(indata) {
        var room_id = indata.field.nId;
        var postData;
        var postUrl;
        if(room_id == ""){
            postUrl = addRoom;
            postData = {name:indata.field.name, location:indata.field.location, ip:indata.field.ip,
                user_id:user_id};
        }else{
            postUrl = editRoom;
            postData = {room_id:room_id, name:indata.field.name, location:indata.field.location, ip:indata.field.ip,
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
                    layer.msg('操作成功');
                    layer.closeAll();
                    $(".layui-laypage-btn").click();
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
    //提交添加数据文件
    $(".module-btn").click(function(){
        //data.field是提交数据
        var dataj = new FormData();
        dataj.append('user_id', user_id);
        dataj.append('s_module', $(".layui-input.modules").val());

        layer.confirm('确认修改？', function(index) {
            $.ajax({
                type: 'post',
                url: global + '/room/set',
                data: dataj,
                dataType: 'json',
                processData: false,
                contentType: false,
                success: function (res) {
                    if (res.code == 0) {//
                        layer.msg('推送成功');
                    } else {
                        layer.msg('操作失败！');
                    }
                },
                error: function (err) {
                    layer.msg('操作失败，请稍后重试！', 'error');
                }
            })
            layer.closeAll();
        })
        return false;
    });


    //初始化显示下拉菜单
    function setModuleSelect(data){
        var roomhtml = '<option value="0" class="tdstyle texts">'+"请选择模块"+'</option>';
        var select;
        for (var i = 1; i < 6; i++){
            if (i == 1) {
                var name = "文字";
            }else  if (i == 2) {
                var name = "图片";
            }else  if (i == 3) {
                var name = "视频";
            }else  if (i == 4) {
                var name = "PPT";
            }
            else  if (i == 5) {
                var name = "课堂";
            }
            if (data.sModule == i) {
                select  = "selected class";
            }else{
                select = " class";
            }
            roomhtml += '<option value="'+i+'" '+ select +'="tdstyle texts">'+name+'</option>';
        }
        $("#modules").html(roomhtml);
        form.render();
    };
});