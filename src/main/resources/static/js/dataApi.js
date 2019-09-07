// var global = 'http://10.69.3.200:8080/ips';
// var global = 'http://pengxiuxiao.55555.io';
var global = 'http://pad.supadata.cn/ips';
// var global = 'http://localhost:8080';


//添加文字消息
var addWord=global + '/notice/addWord';
//编辑文字消息
var editWord=global + '/notice/editWord';
//添加文件消息
var upload=global + '/notice/upload';
//添加文件消息closePad
var eUpload=global + '/notice/eUpload';
//编辑文件消息
var editWord=global + '/notice/editWord';
//删除消息
var deleteWord=global + '/notice/delete';
//添加教室
var addRoom=global + '/room/add';
//编辑教室
var editRoom=global + '/room/edit';
//编辑单个教室显示模块
var setRoom=global + '/room/setone';
//删除教室
var deleteRoom=global + '/room/delete';
//删除卡片
var deleteCard=global + '/card/delete';
//添加卡片
var addCard=global + '/card/add';
//编辑卡片
var editCard=global + '/card/edit';

//下载课程模板
var downCourse=global + '/course/efd';
//上传课程
var uploadCourse=global + '/course/upload';
//编辑课程
var editCourse=global + '/course/edit';
//删除课程
var deleteCourse=global + '/course/delete';

//查询教室列表
var roomList=global + '/room/list';

//pad设置
var setting=global + '/set/set';
//置为黑屏
var blackPad=global + '/pad/blackPad';
//监控
var monitorPad=global + '/pad/monitor';

//app
var uploadApp=global + '/app/upload';


//获取url中user参数
var GetQueryString = function(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  decodeURIComponent(r[2]); return null;
};

//时间格式
Date.prototype.format = function(format) {
    var date = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S+": this.getMilliseconds()
    };
    if (/(y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in date) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
        }
    }
    return format;
}

//获取选择的复选框value
function GetAllChecked(TagName) {
    var checkedvalues = [];

    $("." + TagName + " input[type='checkbox']:checked").each(function () {
        var checkedObj = {};
        checkedObj.id = $(this).attr('data-id');
        checkedObj.name = this.title;
        checkedvalues.push(checkedObj);
    });
    var checkedvlues = JSON.stringify(checkedvalues);
    return checkedvlues;
}

//设置复选框（全选/反选）
function SelectAllCheckBoxOrNot(chAllId, TagName) {
    var allValue = $("#" + chAllId).prop("checked");
    console.log(allValue);
    $("." + TagName + " input[type='checkbox']").prop("checked", allValue);
};
