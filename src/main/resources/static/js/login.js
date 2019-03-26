/**
 * Created by wjw on 2017/9/11.
 */
$(function () {

    localStorage.setItem('user_id','');
    //自动填充用户名和密码
    if ($.cookie("rmbUser") == "true") {
        $("#weuiAgree").attr("checked", true);
        $(".weui-input.userNumber").val($.cookie("userNumber"));
        $(".weui-input.password").val($.cookie("password"));
    }

    //记住用户名密码
    function Save(userNumber, password) {
        if ($("#weuiAgree").is(":checked")) {
            $.cookie("rmbUser", "true", { expires: 7 }); //存储一个带7天期限的cookie
            $.cookie("userNumber", userNumber, { expires: 7 });
            $.cookie("password", password, { expires: 7 });
        }
        else {
            $.cookie("rmbUser", "false", { expire: -1 });
            $.cookie("userNumber", "", { expires: -1 });
            $.cookie("password", "", { expires: -1 });
        }
    };

    var userNumber = '',password = '';
    $('.weui-btn_login').click(function () {
    	userNumber = $('.userNumber').val();
        password = $('.password').val();
        if(userNumber == '' || password == ''){
            $.toptip('用户名或密码不能为空！', 'error');
        } else {
            $.showLoading('正在登录');
             Save(userNumber, password);
             $.ajax({
                 type:'post',
                 url:global + '/pad/pclogin',
                 data:{name:userNumber, pwd:password},
                 dataType:'json',
                 success:function (res) {
                     $.hideLoading();
                     if(res.code == 0){//0
                    	 var user_id = res.data.user_id;
                    	 localStorage.setItem('user_id',user_id);
                    	 window.location.href = 'index.html';
                     }else {
                         $.toptip(res.message, 'error');
                     }
                 },
                 error:function (err) {
                     $.hideLoading();
                     $.toptip('登录失败，请稍后重试！', 'error');
                 }
             })
        }
    })
})