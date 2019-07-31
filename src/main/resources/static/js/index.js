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

    //获取列表接口
    var getOrderList = function (url) {
        table.render({
            elem: '#notice'
            ,url: url
            // ,url: 'js/data.json'
            ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            ,limit: 90
            ,cols: [[
                {type:'checkbox', fixed: 'left'}
                ,{field:'id', title: 'id', width: 80}
                ,{field:'nTitle', title: '标题'}
                ,{field:'nContent', title: '内容'}
                ,{field:'publishRoom', title: '推送教室'} //width 支持：数字、百分比和不填写。你还可以通过 minWidth 参数局部定义当前单元格的最小宽度，layui 2.2.1 新增
                ,{field:'nWordSize', title: '字体大小'}
                ,{field:'fileSize', title: '视频大小(M)'}
                ,{field:'updateTime', title: '操作时间', templet: '#createTime'}
                ,{field:'', title: '操作', templet: '#barDemo', unresize: true, align: 'center', width: 180}
            ]]
            ,page: true
            ,done: function () {
                if (messageType != '1'){
                    $(".preview-a").removeClass('preview-hide');
                }else {
                    $(".preview-a").addClass('preview-hide');
                    $(".preview-e").removeClass('preview-hide');
                }
            }
        });

    };
    var url = global + '/notice/list' + '?user_id='+ user_id + '&type=' + messageType;
    getOrderList(url);

    //搜索
    $(".doSearch").click(function () {
        var content = $(".keyWord").val();
        url = global + '/notice/list' + '?user_id='+ user_id + '&type=' + messageType + '&key=' + content;
        getOrderList(url);
    })


    //发布消息
    $(document).on('click', '.add-msg-btn', function () {
        //填充数据
        $(".layui-input.nId").val('');
        $(".layui-input.title").val('');
        $(".layui-input.content").val('');
        $(".layui-input.word_size").val('');
        $.ajax({
            type:'post',
            url: roomList,
            data:{user_id:user_id,page:1,limit:100},
            dataType:'json',
            success:function (res) {
                if(res.code == 0){//
                   var rooms = res.data;
                   var roomsHTML = '';
                   for(var i = 0; i < rooms.length; i++){
                       roomsHTML += '<input type="checkbox" lay-skin="primary" title="'+ rooms[i].rName
                           +'" data-id="'+ rooms[i].id +'">';
                   }
                   $('.classRooms-check-box').html(roomsHTML);
                    form.render('checkbox');
                }else {
                    layer.msg('操作失败！');
                }

                layer.open({
                    type: 1,
                    title: false,
                    closeBtn: 0,
                    area: '500px',
                    shadeClose: true,
                    content: $('.selectRooms')
                });

            },
            error:function (err) {
                layer.msg('操作失败，请稍后重试！', 'error');
            }

        })
    })
    //全选\反选
    form.on('checkbox(checkAll)', function(data){
        SelectAllCheckBoxOrNot('checkAll', 'classRooms-check-box');
        form.render('checkbox');
    });
    //确定选择教室
    var classRooms = [];
    $(".selectRooms-btn").click(function () {
        layer.closeAll();
        classRooms = GetAllChecked('classRooms-check-box');
        if(messageType === '1'){
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0,
                area: '516px',
                content: $('.modal-content')
            });
        }else if(messageType === '2'){
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0,
                area: '516px',
                content: $('.img-message')
            });
        }else if(messageType === '3'){
            $('.progress > div').css('width', 0);
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0,
                area: '516px',
                content: $('.video-message')
            });
        } else if(messageType === '4'){
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0,
                area: '516px',
                content: $('.ppt-message')
            });
        }

    });
    //文本消息发送
    form.on('submit(addTextBtn)', function(indata){
        //data.field是提交数据
        var dataj = indata.field;
        dataj.room_list = classRooms;
        dataj.user_id = user_id;
        var notice_id =  $(".layui-input.nId").val();
        var postUrl;
        if(notice_id == ""){
            postUrl = addWord;
        }else{
            postUrl = editWord;
            dataj.notice_id = notice_id;
        }
        $.ajax({
            type:'post',
            url: postUrl,
            data:dataj,
            dataType:'json',
            success:function (res) {
                if(res.code == 0){//
                    $(".layui-laypage-btn").click();
                    layer.msg('推送成功');
                }else {
                    layer.msg('操作失败！');
                }
            },
            error:function (err) {
                layer.msg('操作失败，请稍后重试！', 'error');
            }
        })
        layer.closeAll();
        return false;
    });
    //图片消息发送
    $(".addImgBtn").click(function(){
        //data.field是提交数据
        var dataj = new FormData($("#addImgForm")[0]);
        // dataj.room_list = classRooms;
        dataj.append('room_list', classRooms);
        dataj.append('user_id', user_id);
        var notice_id = $(".layui-input.nId").val();
        var postUrl;
        if(notice_id == ""){
            postUrl = upload;
        }else{
            postUrl = eUpload;
            dataj.append('notice_id', notice_id);
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
                    layer.msg('操作失败！');
                }
            },
            error:function (err) {
                layer.msg('操作失败，请稍后重试！', 'error');
            }
        })
        layer.closeAll();
        return false;
    });//视频消息发送
    $(".addVideoBtn").click(function(){
        //data.field是提交数据
        var dataj = new FormData($("#addVideoForm")[0]);
        dataj.append('room_list', classRooms);
        dataj.append('user_id', user_id);
        var notice_id = $(".layui-input.nId").val();
        var postUrl;
        if(notice_id == ""){
            postUrl = upload;
        }else{
            postUrl = eUpload;
            dataj.append('notice_id', notice_id);
        }
        $.ajax({
            type:'post',
            url: postUrl,
            data:dataj,
            dataType:'json',
            processData: false,
            contentType: false,
            beforeSend:function(){
                $("loading").show();
            },
            xhr: function() {
                var xhr = new XMLHttpRequest();
                //使用XMLHttpRequest.upload监听上传过程，注册progress事件，打印回调函数中的event事件
                xhr.upload.addEventListener('progress', function (e) {
                    console.log(e);
                    //loaded代表上传了多少
                    //total代表总数为多少
                    var progressRate = (e.loaded / e.total) * 100 + '%';

                    //通过设置进度条的宽度达到效果
                    $('.progress > div').css('width', progressRate);
                })

                return xhr;
            },
            success:function (res) {
                if(res.code == 0){//

                    layer.closeAll();
                    $("loading").hide();
                    layer.msg('推送成功');
                    $(".layui-laypage-btn").click();
                }else {
                    layer.msg('操作失败！');
                }
            },
            error:function (err) {
                layer.msg('操作失败，请稍后重试！', 'error');
            }
        })
        return false;
    });//ppt消息发送
    $(".addPPTBtn").click(function(){
        //data.field是提交数据
        var dataj = new FormData($("#addPPTForm")[0]);
        dataj.append('room_list', classRooms);
        dataj.append('user_id', user_id);
        var notice_id = $(".layui-input.nId").val();
        var postUrl;
        if(notice_id == ""){
            postUrl = upload;
        }else{
            postUrl = eUpload;
            dataj.append('notice_id', notice_id);
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
                    layer.msg('操作失败！');
                }
            },
            error:function (err) {
                layer.msg('操作失败，请稍后重试！', 'error');
            }
        })
        layer.closeAll();
        return false;
    });
    //点击预览
    table.on('tool(order-table)', function(obj){
        if(obj.event === 'preview'){

            var preview = $("#preview");
            var nUrl = obj.data.nUrl;
            if(messageType === '2'){
                if (nUrl == '' || nUrl == null){
                    layer.msg('暂无图片');
                }else {
                    preview.find('img').attr('src', nUrl);
                    layer.msg('正在加载图片···', {
                        icon: 16
                        ,shade: 0.01
                    });
                    setTimeout(function () {
                        layer.open({
                            type: 1,
                            title: false,
                            closeBtn: 0,
                            area: '516px',
                            skin: 'layui-layer-nobg', //没有背景色
                            shadeClose: true,
                            content: preview
                        });
                    }, 2000);
                }
            }else if(messageType === '3'){
                if (nUrl == '' || nUrl == null){
                    layer.msg('暂无视频');
                }else {
                    layer.msg('正在加载视频···', {
                        icon: 16
                        ,shade: 0.01
                    });
                    setTimeout(function () {
                        layer.open({
                            type: 2,
                            title: false,
                            closeBtn: 0,
                            area: '516px',
                            skin: 'layui-layer-nobg', //没有背景色
                            shadeClose: true,
                            content: nUrl
                        });
                    }, 2000);
                }
            }else if(messageType === '4'){
                if (nUrl == '' || nUrl == null){
                    layer.msg('暂无图片');
                }else {
                    preview.find('img').attr('src', nUrl);
                    layer.msg('正在加载图片···', {
                        icon: 16
                        ,shade: 0.01
                    });
                    setTimeout(function () {
                        layer.open({
                            type: 1,
                            title: false,
                            closeBtn: 0,
                            area: '516px',
                            skin: 'layui-layer-nobg', //没有背景色
                            shadeClose: true,
                            content: preview
                        });
                    }, 2000);
                }
            }

        }else if(obj.event === 'edit'){//编辑
            $(".layui-input.nId").val(obj.data.id);
            $(".layui-input.title").val(obj.data.nTitle);
            $(".layui-input.content").val(obj.data.nContent);
            $(".layui-input.word_size").val(obj.data.nWordSize);
            if(messageType === '1'){
                layer.open({
                    type: 1,
                    title: false,
                    closeBtn: 0,
                    area: '520px',
                    shadeClose: true,
                    content: $('.modal-content')
                });
            }else if(messageType === '2'){
                layer.open({
                    type: 1,
                    title: false,
                    closeBtn: 0,
                    area: '520px',
                    shadeClose: true,
                    content: $('.img-message')
                });
            }else if(messageType === '3'){
                layer.open({
                    type: 1,
                    title: false,
                    closeBtn: 0,
                    area: '520px',
                    shadeClose: true,
                    content: $('.video-message')
                });
            }else if(messageType === '4'){
                layer.open({
                    type: 1,
                    title: false,
                    closeBtn: 0,
                    area: '520px',
                    shadeClose: true,
                    content: $('.ppt-message')
                });
            }
        }else if(obj.event === 'del'){//删除
            layer.confirm('确认删除 ' + obj.data.nTitle + '？', function(index){
                obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                layer.close(index);
                //向服务端发送删除指令
                $.ajax({
                    type:'post',
                    url: deleteWord,
                    data:{notice_id:obj.data.id, user_id:user_id},
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
                        layer.msg('操作失败，请稍后重试！');
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
        },
        word_size: function(value){
            if(value == '' || value == null){
                return '请输入字体大小！';
            }
            if (!isNumber(value)) {
                return '字体大小必须是大于0的数字！';
            }
        }

    });

    //判读值是否是数字
    function isNumber(val){

        var regPos = /^\d+(\.\d+)?$/; //非负浮点数
        var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
        if(regPos.test(val) || regNeg.test(val)){
            if (val <= 0) {//小于0也不行
                return false;
            }
            return true;
        }else{
            return false;
        }

    }
    //取消按钮
    $(".cancel-btn").click(function () {
        layer.closeAll();
    })

    //批量删除
    $('.bdnotice-btn').on('click', function(){
        var checkStatus = table.checkStatus('notice');
        var data = JSON.stringify(checkStatus.data);
        if (data == "" || data == "[]") {
            layer.msg('请先选择Pad！');
            return;
        }
        layer.confirm('确认批量删除? ', function(){
            //ajax调用后台添加接口
            $.ajax({
                type:'post',
                url: global + '/notice/bdn',
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

//    切换消息类型，刷新数据
    form.on('select(msg-type)', function(data){
        messageType = data.value;
        var content = $(".keyWord").val();
        url = global + '/notice/list' + '?user_id='+ user_id + '&type=' + messageType + '&key=' + content;
        getOrderList(url);
    });

   //上传图片
    $("#upload-PPT").change(function () {
        var self = this
        if(self.files && self.files[0]){
            var fileName = self.files[0].name;
            var index = fileName.lastIndexOf('.');
            var fileType = fileName.substring(index + 1, fileName.length);
            console.log(fileType);
            if (fileType === 'ppt' || fileType === 'pptx'){
            }else{
                $('#upload-PPT').val('');
                layer.msg('请选择PPT文件', function(){
                    //关闭后的操作
                });
            }
        }
    })
});