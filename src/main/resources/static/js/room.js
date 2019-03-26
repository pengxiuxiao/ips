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
    	
        table.render({
            elem: '#test'
            ,url: url
            // ,url: 'js/data.json'
            ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            ,limit: 20
            ,cols: [[
                {field:'id', title: 'id', width: 80}
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

});