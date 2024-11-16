$(document).ready(function () {
    $('#sendCode-button').on('click', function (e) {
        e.preventDefault();
        const email = $('#forgotPasswordEmail').val();
        if (email === "") {
            $('.form-message-email').text("Vui lòng nhập email");
            return;
        }
        const sendCodeButton = $('#sendCode-button');
        function startCountdown(duration) {
            let timeRemaining = duration;
            sendCodeButton.prop('disabled', true);
            sendCodeButton.text(`${timeRemaining}s`);
            const countdownInterval = setInterval(() => {
                timeRemaining--;
                if (timeRemaining <= 0) {
                    clearInterval(countdownInterval);
                    sendCodeButton.prop('disabled', false);
                    sendCodeButton.text("Gửi mã");
                } else {
                    sendCodeButton.text(`${timeRemaining}s`);
                }
            }, 1000);
        }
        $.ajax({
            url: 'http://localhost:8080/user/forgot/send',
            type: 'POST',
            data: { email: email },
            success: function (response) {
                console.log(response);
                if (response.success) {
                    toast({
                        title: "Success",
                        message: "Đã gửi mã về email của bạn !",
                        type: "success",
                        duration: 2000,
                    });
                    startCountdown(60);
                } else {
                    if(response.description === "Wait 60s!"){
                        startCountdown(60);
                    }
                    else if(response.description === "User not found"){
                        toast({
                            title: "Error",
                            message: "Tài khoản không tồn tại !",
                            type: "error",
                            duration: 2000,
                        });
                    }
                    else{
                        toast({
                            title: "Warning",
                            message: "Gửi mã thất bại !",
                            type: "warning",
                            duration: 2000,
                        });
                    }

                }
            },
            error: function () {
                toast({
                    title: "Error",
                    message: "Có lỗi ở server !",
                    type: "error",
                    duration: 2000,
                });
            }
        });
    });

    function verifyCode() {
        const email = $('#forgotPasswordEmail').val();
        const code = $('#verificationCode').val();
        if (code) {
            $.ajax({
                url: 'http://localhost:8080/user/forgot/verify',
                type: 'POST',
                data: { email: email, code: code },
                success: function (response) {
                    if (response.success) {
                        $('.password-group, .confirm-password-group').removeClass('hidden');
                        $('#verificationCodeError')
                            .removeClass('text-danger')
                            .addClass('text-success')
                            .text("Mã xác minh hợp lệ. Vui lòng nhập mật khẩu mới.");
                    } else {
                        if (response.description === "Expired") {
                            $('#verificationCodeError')
                                .removeClass('text-success')
                                .addClass('text-danger')
                                .text("Mã xác minh đã hết hạn.");
                        } else if (response.description === "Invalid code") {
                            $('#verificationCodeError')
                                .removeClass('text-success')
                                .addClass('text-danger')
                                .text("Mã xác minh không hợp lệ.");
                        }
                    }
                },
                error: function () {
                    $('.code-error').text("Đã xảy ra lỗi. Vui lòng thử lại.");
                }
            });
        }
    }

    $('#verificationCode').on('blur', function () {
        verifyCode();
    });

    $('#reset-button').on('click', function (e) {
        e.preventDefault();
        const email = $('#forgotPasswordEmail').val();
        const code = $('#verificationCode').val();
        const newPassword = $('#newPassword').val();
        const confirmNewPassword = $('#confirmNewPassword').val();

        if(newPassword) {
            if(newPassword.length < 6) {
                $('.confirm-password-error').text("Mật khẩu mới phải có 6 kí tự trở lên.");
                return;
            }
        }

        if (newPassword && confirmNewPassword) {
            if (newPassword !== confirmNewPassword) {
                $('.confirm-password-error').text("Mật khẩu không khớp. Vui lòng thử lại.");
                return;
            }
            $.ajax({
                url: 'http://localhost:8080/user/forgot/reset/password',
                type: 'POST',
                data: {
                    email: email,
                    code: code,
                    newPassword: newPassword
                },
                contentType: 'application/x-www-form-urlencoded',
                success: function(response) {
                    console.log(response);
                    if (response.success) {
                        toast({
                            title: "Success",
                            message: "Đã đổi mật khẩu thành công !",
                            type: "success",
                            duration: 2000,
                        });
                        $('#forgotPasswordModal').removeClass('open');
                    } else {
                        if(response.exist){
                            toast({
                                title: "Warning",
                                message: "Mật khẩu mới phải khác mật khẩu cũ !",
                                type: "warning",
                                duration: 2000,
                            });
                            $('#newPassword').val('');
                            $('#confirmNewPassword').val('');
                        }
                        else {
                            toast({
                                title: "Error",
                                message: "Đổi mật khẩu không thành công !",
                                type: "error",
                                duration: 2000,
                            });
                        }
                    }
                },
                error: function(xhr, status, error) {
                    console.log("Error: " + error);
                }
            });
        }
    });
});
