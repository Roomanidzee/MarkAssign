$( document ).ready(function() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/get/organizations",
        dataType: 'json',
        success: function (result) {
            var option = "";
            var c = 0;
            result.forEach(function (i, org) {
                option += "<option value=" + c + ">" + result[org] + "</option>";
                c++;
            });
            $("#organizations-list").html(option);
        },
        error: function (e) {
            alert("Error!");
            console.log("ERROR: ", e);
        }
    });
    $('#degrees-list').change(function () {
        ajaxPostDegree($("#degrees-list").val());
    });

    $('#course-list').change(function () {
        console.log($("#course-list").val(), $("#degrees-list").val());
        ajaxPostCourse($("#course-list").val(), $("#degrees-list").val());
    });

    $('#degrees-list-modal').change(function () {
        ajaxPostDegree($("#degrees-list-modal").val());
    });

    $('#course-list-modal').change(function () {
        console.log($("#course-list-modal").val(), $("#degrees-list-modal").val());
        ajaxPostCourse($("#course-list-modal").val(), $("#degrees-list-modal").val());
    });

    function ajaxPostDegree(degree) {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/get/courses",
            data: JSON.stringify(degree),
            dataType: 'json',
            success: function (result) {
                var option = "";
                var c = 0;
                result.forEach(function (i, course) {
                    option += "<option value=" + c + ">" + result[course] + "</option>";
                    c++;
                })
                $("#course-list").html(option);
                $("#course-list-modal").html(option);
            },
            error: function (e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });
    }

    function ajaxPostCourse(course, degree) {
        console.log(course, degree);
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/get/groups",
            data: JSON.stringify({ course: course,
                degree: degree }) ,
            dataType: 'json',
            success: function (result) {
                var option = "";
                result.forEach(function (i, group) {
                    option +=  "<option>" + result[group] + "</option>";
                })
                $("#group-list").html(option);
                $("#group-list-modal").html(option);
            },
            error: function (e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });
    }
});