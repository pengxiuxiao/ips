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
            elem: '#test'
            ,url: url
            // ,url: 'js/data.json'
            ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            ,limit: 90
            ,cols: [[
                {field:'id', title: 'id', width: 80}
                ,{field:'aVersion', title: '版本号', width: 150}
                // ,{field:'aType', title: '版本类型', width: 150}
                ,{field:'aDesc', title: '版本描述'}
                ,{field:'updateTime', title: '更新时间', templet: '#createTime', width: 180}
                ,{field:'aUrl', title: '下载链接'}
                // ,{field:'', title: '操作', templet: '#barDemo', unresize: true, align: 'center', width: 150}
            ]]
            ,page: true
        });

    };
    var url = global + '/app/list' + '?user_id='+ user_id;
    getOrderList(url);



    //发布新版
    $(document).on('click', '.add-app-btn', function () {
        //填充数据
        $(".layui-input.version").val('');
        $(".layui-input.desc").val('');
        //获取教室列表
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: '516px',
            content: $('.app-content')
        });
    });
    //提交添加数据文件
    $(".addAppBtn").click(function(){
        //data.field是提交数据
        var dataj = new FormData($("#addAppForm")[0]);
        dataj.append('user_id', user_id);
        $.ajax({
            type:'post',
            url: uploadApp,
            data:dataj,
            dataType:'json',
            processData: false,
            contentType: false,
            success:function (res) {
                if(res.code == 0){//
                    layer.msg('发布成功');
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

    //取消按钮
    $(".cancel-btn").click(function () {
        layer.closeAll();
    })


    //上传excel
    $("#upload-excel").change(function () {
        var self = this
        if(self.files && self.files[0]){
            var fileName = self.files[0].name;
            var index = fileName.lastIndexOf('.');
            var fileType = fileName.substring(index + 1, fileName.length);
            console.log(fileType);
            if (fileType === 'apk' || fileType === 'zip'){
            }else{
                $('#upload-app').val('');
                layer.msg('请选择apk或者zip文件', function(){
                    //关闭后的操作
                });
            }
        }
    })




});