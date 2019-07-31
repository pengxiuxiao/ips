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
            elem: '#pads'
            ,url: url
            // ,url: 'js/data.json'
            ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            ,limit: 90
            ,cols: [[
                {type:'checkbox', fixed: 'left'}
                ,{field:'id', title: 'id', width: 80}
                ,{field:'code', title: 'code标识'}
                ,{field:'roomName', title: '所属教室'}
                ,{field:'startTime', title: '开机时间'}
                ,{field:'endTime', title: '关机时间'}
                ,{field:'pClickCard', title: '刷卡提示'}
                ,{field:'pState', title: '是否锁屏'}
                ,{field:'pAudio', title: '音量大小'}
                ,{field:'pModule', title: '显示模块'}
                // ,{field:'', title: '操作', templet: '#barDemo', unresize: true, align: 'center', width: 180}
            ]]
        ,page: true
        });
    };
    var url = global + '/pad/list' + '?user_id='+ user_id + '&status=open';
    getOrderList(url);

    //搜索
    $(".doSearch").click(function () {
        var content = $(".keyWord").val();
        url = global + '/pad/list' + '?user_id='+ user_id + '&status=open' + '&key=' + content;
        getOrderList(url);
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


    //取消按钮
    $(".cancel-btn").click(function () {
        layer.closeAll();
    })




    //批量设置打卡提示
    $('.bdpad-sk').on('click', function(){
        var checkStatus = table.checkStatus('pads');
        var data = JSON.stringify(checkStatus.data);
        if (data == "" || data == "[]") {
            layer.msg('请先选择Pad！');
            return;
        }
        localStorage.setItem('card_idList',data);
        //打开显示下拉框
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: '516px',
            shadeClose: true,
            content: $('.set-card')
        });

    });


    //监听打卡提交按钮
    form.on('submit(addCardbtn)', function(indata){
        //ajax调用后台添加接口
        $.ajax({
            type:'post',
            url: global + '/set/bscard',
            data:{idList:localStorage.getItem("card_idList"), user_id:user_id, status:indata.field.card},
            dataType:'json',
            success:function (res) {
                if(res.code == 0){//0
                    layer.msg('操作成功');
                    layer.closeAll();
                    $(".layui-laypage-btn").click();
                }else {
                    layer.msg('操作失败');
                }
            },
            error:function (err) {
                layer.msg('操作失败');
            }
        })
        return false;
    });

    //批量设置锁屏
    $('.bdpad-state').on('click', function(){
        var checkStatus = table.checkStatus('pads');
        var data = JSON.stringify(checkStatus.data);
        if (data == "" || data == "[]") {
            layer.msg('请先选择Pad！');
            return;
        }
        localStorage.setItem('sate_idList',data);
        //打开显示下拉框
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: '516px',
            shadeClose: true,
            content: $('.set-state')
        });

    });

    //监听锁屏提交按钮
    form.on('submit(addStatebtn)', function(indata){
        //ajax调用后台添加接口
        $.ajax({
            type:'post',
            url: global + '/set/bscard',
            data:{idList:localStorage.getItem("sate_idList"), user_id:user_id, state:indata.field.state},
            dataType:'json',
            success:function (res) {
                if(res.code == 0){//0
                    layer.msg('操作成功');
                    layer.closeAll();
                    $(".layui-laypage-btn").click();
                }else {
                    layer.msg('操作失败');
                }
            },
            error:function (err) {
                layer.msg('操作失败');
            }
        })
        return false;
    });

    //批量设置音量
    $('.bdpad-audio').on('click', function(){
        var checkStatus = table.checkStatus('pads');
        var data = JSON.stringify(checkStatus.data);
        if (data == "" || data == "[]") {
            layer.msg('请先选择Pad！');
            return;
        }
        localStorage.setItem('audio_idList',data);
        //打开显示下拉框
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: '516px',
            shadeClose: true,
            content: $('.set-audio')
        });

    });

    //监听音量提交按钮
    form.on('submit(addAudiobtn)', function(indata){
        //ajax调用后台添加接口
        $.ajax({
            type:'post',
            url: global + '/set/bscard',
            data:{idList:localStorage.getItem("audio_idList"), user_id:user_id, audio:indata.field.audio},
            dataType:'json',
            success:function (res) {
                if(res.code == 0){//0
                    layer.msg('操作成功');
                    layer.closeAll();
                    $(".layui-laypage-btn").click();
                }else {
                    layer.msg('操作失败');
                }
            },
            error:function (err) {
                layer.msg('操作失败');
            }
        })
        return false;
    });

    //批量设置开关机
    $('.bdpad-close').on('click', function(){
        var checkStatus = table.checkStatus('pads');
        var data = JSON.stringify(checkStatus.data);
        if (data == "" || data == "[]") {
            layer.msg('请先选择Pad！');
            return;
        }
        //日期时间选择器
        laydate.render({
            elem: '#end_time'
            ,value: null
            ,type: 'datetime'
        });
        //日期时间选择器
        laydate.render({
            elem: '#start_time'
            ,type: 'datetime'
        });
        localStorage.setItem('close_idList',data);
        //打开显示下拉框
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: '516px',
            shadeClose: true,
            content: $('.set-close')
        });

    });

    //监听开关机提交按钮
    form.on('submit(addClosebtn)', function(indata){
        //ajax调用后台添加接口
        $.ajax({
            type:'post',
            url: global + '/set/bscard',
            data:{idList:localStorage.getItem("close_idList"), user_id:user_id,
                start_time:$(".layui-input.start_time").val(), end_time:$(".layui-input.end_time").val()},
            dataType:'json',
            success:function (res) {
                if(res.code == 0){//0
                    layer.msg('操作成功');
                    layer.closeAll();
                    $(".layui-laypage-btn").click();
                }else {
                    layer.msg(res.msg);
                }
            },
            error:function (err) {
                layer.msg('操作失败');
            }
        })
        return false;
    });

    //批量设置显示模块
    $('.bdpad-module').on('click', function(){
        var checkStatus = table.checkStatus('pads');
        var data = JSON.stringify(checkStatus.data);
        if (data == "" || data == "[]") {
            layer.msg('请先选择Pad！');
            return;
        }
        //日期时间选择器
        laydate.render({
            elem: '#end_time'
            ,value: null
            ,type: 'datetime'
        });
        //日期时间选择器
        laydate.render({
            elem: '#start_time'
            ,type: 'datetime'
        });
        localStorage.setItem('close_idList',data);
        //打开显示下拉框
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: '516px',
            shadeClose: true,
            content: $('.set-module')
        });

    });

    //监听显示提交按钮
    form.on('submit(addModulebtn)', function(indata){
        //ajax调用后台添加接口
        $.ajax({
            type:'post',
            url: global + '/set/bsv',
            data:{idList:localStorage.getItem("close_idList"), user_id:user_id, modules:indata.field.modules},
            dataType:'json',
            success:function (res) {
                if(res.code == 0){//0
                    layer.closeAll();
                    $(".layui-laypage-btn").click();
                    if (indata.field.modules == "3") {
                        layer.open({
                            title: '消息'
                            , content: res.msg
                        });
                    } else {
                        layer.msg(res.msg);
                    }
                }else {
                    layer.msg('操作失败');
                }
            },
            error:function (err) {
                layer.msg('操作失败');
            }
        })
        return false;
    });

    //点击刷新
    $(document).on('click', '.add-refresh-btn', function () {
        $(".layui-laypage-btn").click();
    })


});