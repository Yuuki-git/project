layui.use(['form','jquery'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery;

    // 为密码文本框填充值
    // if ($("#password").val() != null && $("#password").val() != '') {
    //     $("[name='password']").val(window.atob($("#password").val()));
    // }

    // 表单提交
    form.on('submit(login)', function(data){
        //当前容器的全部表单字段，名值对形式: {name: value}
        console.log(data.field);
        $.ajax({
            type:"post",
            url:"user/login",
            data:{
                userName: data.field.userName,
                userPwd:data.field.password
            },
            success: function (result) { // result是回调函数，用来接收后端返回的数据
                console.log(result);
                if (result.code == 200) {
                    //登录成功
                    /**
                     *  设置用户是登录状态
                     *
                     */
                    layer.msg("登录成功! ",function (){
                        //判断用户是否选择记住密码(判断复选框是否选中)
                        if($("#rememberMe").prop("checked")){
                            //选中
                            //将用户信息设置到cookie
                            $.cookie("userIdStr",result.result.userIdStr,{expires:7});
                            $.cookie("userName",result.result.userName,{expires:7});
                            $.cookie("trueName",result.result.trueName,{expires:7});
                        } else {
                            //未选中
                            //将用户信息设置到cookie
                            $.cookie("userIdStr",result.result.userIdStr);
                            $.cookie("userName",result.result.userName);
                            $.cookie("trueName",result.result.trueName);
                        }

                        //将用户信息设置到cookie
                        // $.cookie("userIdStr",result.result.userIdStr);
                        // $.cookie("userName",result.result.userName);
                        // $.cookie("trueName",result.result.trueName);
                        //登陆成功 跳转首页
                        window.location.href="main";
                    })
                } else{
                    //登录失败
                    layer.msg(result.msg,{icon:5});
                }
            }
        });
        // 防止多次点击，使得登录按钮暂时失效
        // $("[lay-filter='login']").toggleClass("layui-btn-disabled");
        // $("[lay-filter='login']").attr("disabled","disabled");
        // 由于密码比较特殊，提前保存到cookie中
        // if ($("#rememberMe").prop("checked")) {
        //     $.cookie("password", window.btoa($("[name='password']").val()), {expires: 7});
        // } else {
        //     $.cookie("password", window.btoa($("[name='password']").val()));
        // }
        // $.post(
        //     "user/login",
        //     {
        //         userName: data.field.userName,
        //         password: data.field.password
        //     },
            // function (data) {
                // if (data.code == 200) {
                //     layer.msg(data.msg,function () {
                //         // 登录成功
                //         if ($("#rememberMe").prop("checked")) {
                //             $.cookie("userId", data.result.userId,{expires: 7});
                //             $.cookie("userName", data.result.userName, {expires: 7});
                //             $.cookie("trueName", data.result.trueName, {expires: 7});
                //         } else {
                //             $.cookie("userId",data.result.userId);
                //             $.cookie("userName",data.result.userName);
                //             $.cookie("trueName",data.result.trueName);
                //         }
                //         // 执行跳转方法
                //         window.location.href="main";
                //     });
                // } else {
                //     // 登录失败
                //     layer.msg(data.msg, {icon: 5}, function () {
                //         // 登录失败，则恢复登录按钮点击事件
                //         $("[lay-filter='login']").toggleClass("layui-btn-disabled");
                //         $("[lay-filter='login']").removeAttr("disabled");
                //     });
                // }
            // }
        // );
        return false;
    });
});
