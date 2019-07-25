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
                setModuleSelect(data, "1");
            }
        })

        table.render({
            elem: '#rooms'
            ,url: url
            // ,url: 'js/data.json'
            ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            ,limit: 90
            ,cols: [[
                {field:'id', title: 'id', width: 80}
                ,{field:'rName', title: '教室名'}
                ,{field:'rIp', title: '教室Code'} //width 支持：数字、百分比和不填写。你还可以通过 minWidth 参数局部定义当前单元格的最小宽度，layui 2.2.1 新增
                ,{field:'rModuleMsg', title: '显示模块'}
                // ,{field:'updateTime', title: '操作时间', templet: '#createTime'}
                ,{field:'', title: '操作', templet: '#barDemo', unresize: true, align: 'center', width: 180}
            ]]
        ,page: true
        });
    };
    var url = global + '/room/list' + '?user_id='+ user_id;
    getOrderList(url);

    //点击编辑 (具体类名自己替换)
    table.on('tool(order-form)', function(obj){
        if(obj.event === 'edit'){//编辑
            setModuleSelect(obj.data, "2");
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
        var postData = {room_id:room_id, s_module:indata.field.module, user_id:user_id};

        //ajax调用后台添加接口
        $.ajax({
            type:'post',
            async: false,
            url:setRoom,
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
        dataj.append('room_id', $(".layui-input.nId").val());

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
                        $(".layui-laypage-btn").click();
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
    function setModuleSelect(data, flag){
        var m;
        if (flag == "1") {
            m = data.sModule;
        }else {
            m = data.rModule;
        }
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
                var name = "课程";
            }
            if (m == i) {
                select  = "selected class";
            }else{
                select = " class";
            }
            roomhtml += '<option value="'+i+'" '+ select +'="tdstyle texts">'+name+'</option>';
        }
        if (flag == "1") {
            $("#modules").html(roomhtml);
        }else {
            $("#module").html(roomhtml);
        }
        form.render();
    };


});