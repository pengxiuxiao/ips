layui.use(['element', 'table', 'laydate', 'jquery','upload'], function(){
    var laydate = layui.laydate,
        layer = layui.layer,
        form = layui.form,
        table = layui.table;

    var user_id = localStorage.getItem('user_id');
    if(user_id == null || user_id == ''){
        window.location.href = 'login.html';
    }
    //首次渲染列表
    //获取列表接口
    var getOrderList = function (url) {
        $.ajax({
            url:url,
            type:"post",
            data:{"code":user_id},
            dataType:"json",
            success:function (data) {
                var data = data.data;
                setModuleSelect(data);
                setDateTime(data);
                setCheckBox(data);
                setdaojishi(data);
                setRotationTime(data);
                setFontSize(data);
                setFontColor(data);
            }
        })

    };
    var url = global + '/pad/setting' + '?code='+ user_id ;
    getOrderList(url);

    //提交添加数据文件
    $(".confirm").click(function(){
        //data.field是提交数据

        var dataj = new FormData();
        dataj.append('user_id', user_id);
        dataj.append('start_time', $(".layui-input.start_time").val());
        dataj.append('end_time', $(".layui-input.end_time").val());

        dataj.append('display_date', $('#date_time').prop('checked'));
        dataj.append('display_card', $('#card_notice').prop('checked'));
        dataj.append('display_daojishi', $('#disply_daojishi').prop('checked'));
        dataj.append('word_font', $('#lock_pad').prop('checked'));
        dataj.append('seRemark', $('#void').prop('checked'));

        dataj.append('daojishi', $(".layui-input.daojishi").val());
        dataj.append('word_size', $(".layui-input.size").val());
        dataj.append('word_color', $(".layui-input.color").val());
        dataj.append('rotation_time', $(".layui-input.rotation_time").val());
        dataj.append('s_module', $(".layui-input.modules").val());

        layer.confirm('确认修改？', function(index) {
            $.ajax({
                type: 'post',
                url: setting,
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


    //初始化复选框
    function setCheckBox(data){

        if (data.displayDate == '0') {
            $(".layui-input.date_time").prop('checked', true);
        }if (data.displayCard == '0') {
            $(".layui-input.card_notice").prop('checked', true);
        }if (data.displayDaojishi == '0') {
            $(".layui-input.disply_daojishi").prop('checked', true);
        }
        if (data.wordFont == '0') {
            $(".layui-input.lock_pad").prop('checked', true);
        }
        if (data.seRemark == '0') {
            $(".layui-input.void").prop('checked', true);
        }
        form.render('checkbox');
    };

    //初始化显示倒计时
    function setdaojishi(data){
        var roomhtml = '<option value="0" class="tdstyle texts">'+"请选择倒计时(min)"+'</option>';
        var select;
        for (var i = 1; i < 5; i++){
            if ((i * 5) == 5) {
                var name = "5min";
            }else  if ((i * 5) == 10) {
                var name = "10min";
            }else  if ((i * 5) == 15) {
                var name = "15min";
            }else  if ((i * 5) == 20) {
                var name = "20min";
            }
            if ((i * 5) == data.daojishi) {
                select  = "selected class";
            }else{
                select = " class";
            }
            roomhtml += '<option value="' + (i * 5) + '" '+ select +'="tdstyle texts">'+name+'</option>';
        }
        $("#daojishi").html(roomhtml);
        form.render();
    };

    //初始化显示轮播
    function setRotationTime(data){
        var roomhtml = '<option value="0" class="tdstyle texts">'+"请选择周期(s)"+'</option>';
        var select;
        for (var i = 1; i < 6; i++){
            if (i == 1) {
                var name = "1s";
            }else  if (i == 2) {
                var name = "2s";
            }else  if (i == 3) {
                var name = "3s";
            }else  if (i == 4) {
                var name = "4s";
            }else  if (i == 5) {
                var name = "5s";
            }
            if (i == data.rotationTime) {
                select  = "selected class";
            }else{
                select = " class";
            }
            roomhtml += '<option value="' + i + '" '+ select +'="tdstyle texts">'+name+'</option>';
        }
        $("#rotation_time").html(roomhtml);
        form.render();
    };

    //初始化显示下拉菜单
    function setModuleSelect(data){
        var roomhtml = '<option value="0" class="tdstyle texts">'+"请选择模块"+'</option>';
        var select;
        for (var i = 1; i < 5; i++){
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

    //初始化字号
    function setFontSize(data){
        var roomhtml = '<option value="0" class="tdstyle texts">'+"请选择字号"+'</option>';
        var select;
        for (var i = 1; i < 4; i++){
            if (i == 1) {
                var name = "80";
            }else  if (i == 2) {
                var name = "90";
            }else  if (i == 3) {
                var name = "100";
            }

            if (data.wordSize == ((i+7)*10)) {
                select  = "selected class";
            }else if (data.wordSize == ((i+7)*10)) {
                select = "selected class";
            }else if (data.wordSize == ((i+7)*10)) {
                select = "selected class";
            }else{
                select = " class";
            }
            roomhtml += '<option value="'+((i+7)*10)+'" '+ select +'="tdstyle texts">'+name+'</option>';
        }
        $("#size").html(roomhtml);
        form.render();
    };
    //初始化字颜色
    function setFontColor(data){
        var roomhtml = '<option value="0" class="tdstyle texts">'+"请选择颜色"+'</option>';
        var select;
        var arry = new Array()
        arry[0]="红"
        arry[1]="白"
        arry[2]="黑"
        for (var i = 0; i < arry.length; i++){
            if (i == 0) {
                var name = "红";
            }else  if (i == 1) {
                var name = "白";
            }else  if (i == 2) {
                var name = "黑";
            }

            if (data.wordColor == arry[i]) {
                select  = "selected class";
            }else{
                select = " class";
            }
            roomhtml += '<option value="'+ name +'" '+ select +'="tdstyle texts">'+name+'</option>';
        }
        $("#color").html(roomhtml);
        form.render();
    };

    //初始化开关机时间
    function setDateTime(data){
        $(".layui-input.start_time").val(data.sStartTime);
        $(".layui-input.end_time").val(data.sEndTime);
        //日期时间选择器
        laydate.render({
            elem: '#end_time'
            ,value: new Date(data.sEndTime).format('yyyy-MM-dd hh:mm:ss')
            ,type: 'datetime'
        });
        //日期时间选择器
        laydate.render({
            elem: '#start_time'
            ,value: new Date(data.sStartTime).format('yyyy-MM-dd hh:mm:ss')
            ,type: 'datetime'
        });
    }
});
