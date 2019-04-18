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
    var getCardList = function (url) {
    	
        table.render({
            elem: '#cards'
            ,url: url
            // ,url: 'js/data.json'
            ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            ,limit: 20
            ,cols: [[
                {type:'checkbox', fixed: 'left'}
                ,{field:'id', title: 'id', width: 80}
                ,{field:'studentName', title: '持卡人'}
                ,{field:'cardNumber', title: '卡号'}
                ,{field:'updateTime', title: '操作时间', templet: '#createTime'}
                ,{field:'', title: '操作', templet: '#barDemo', unresize: true, align: 'center', width: 150}
            ]]
        ,page: true
        });
    };
    var url = global + '/card/list' + '?user_id='+ user_id;
    getCardList(url);

    //搜索
    $(".doSearch").click(function () {
        var content = $(".keyWord").val();
        url = global + '/card/list' + '?user_id='+ user_id + '&number=' + content;
        getCardList(url);
    })


    //点击添加
    $(document).on('click', '.add-card-btn', function () {
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
            $(".layui-input.cId").val(obj.data.id);
            $(".layui-input.name").val(obj.data.studentName);
            $(".layui-input.number").val(obj.data.cardNumber);
        }else if(obj.event === 'del'){//删除
            layer.confirm('确认删除 ' + obj.data.cardNumber + '？', function(index){
                $.ajax({
                    type:'post',
                    url: deleteCard,
                    data:{id:obj.data.id, user_id:user_id},
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
    $('.bdcard-btn').on('click', function(){
        var checkStatus = table.checkStatus('cards')
            ,data = checkStatus.data;
        // layer.alert(JSON.stringify(data));
        //ajax调用后台添加接口
        $.ajax({
            type:'post',
            url: global + '/card/bdc',
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
        var c_id = indata.field.cId;
        var postData;
        var postUrl;
        if(c_id == ""){
            postUrl = addCard;
            postData = {cardNo:indata.field.number, name:indata.field.name, user_id:user_id};
        }else{
            postUrl = editCard;
            postData = {id:indata.field.cId, name:indata.field.name, user_id:user_id};
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
                    layer.msg(res.msg);
                }
            },
            error:function (err) {
                $.hideLoading();
                $.toptip('请求失败，请稍后重试！', 'error');
            }
        })
    }

});