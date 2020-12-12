/* 
 * Author: jianqing
 * Date: May 18, 2020
 * Description: This document is created for champions key as a sub site.
 */

const SUPPORT_EMAIL = "support@gaogato.com";
var activeNotified = false;
function initPio()
{
    console.log("start load pio process");
    //initalize the girl
    pio = new Paul_Pio({
        "mode": "draggable",
        "hidden": false,
        "content": {
            "welcome": ["Welcome to use champions key! Any doubt? contact support@gaogato.com!", "Hi, welcome! Any doubt? contact support@gaogato.com!"],
            "custom": [
                //{"selector": ".comment-form", "text": "欢迎参与本文评论，别发小广告噢~"},
                //{"selector": ".home-social a:last-child", "text": "在这里可以了解博主的日常噢~"},
                //{//"selector": ".post-item a", "type": "read"},
                //{"selector": ".post-content a, .page-content a", "type": "link"},
                //{"selector": "#nolikebtn", "text": "Heng! You will regret for clicking this!"},
                //{"selector": "#email", "type": "click", "text": "Please use your school email"}

            ],
            "touch": "Thank you for touching me! mmm~",
            "copy": []
        },
        /* "night": "alert('night mode is not supported. Sorry\n\
         !')",*/
        "model": ["models/pio/model.json"],
        "referer": "Welcome my friend from %t"


    });
}

//'Hi, ${staff.getUsername()} , ${activeDivCss.contains('block')?'If you want to begin sign - in process, please click on \'Students Sign-in Panel\'. If you wish to end this class, please click \'end session\'. ':' If you wish to start a class, please click \'start a studyhall\' To view/manage your history, click \'show\' under \'Your History Attendances\''}'
function helpcount(seconds, message)
{
    var i = 0;
    var x = setInterval(function () {
        i++;
        console.log(i);
        if (i === seconds)
        {
            notify(message);
            clearInterval(x);
        }
    }, 1000);
}

function randomMember(arr) {
    return arr[Math.floor(Math.random() * arr.length + 1) - 1];
}

/**
 * This makes the Pio girl to speak. It will find the parent pio to speak. You may assign array here.
 * You can also write html for message.
 * @param {type} msg
 * @returns {undefined}
 */
function notify(msg)
{
    //var activeNotified = window.localStorage.getItem('activeNotified');
    if (msg.constructor === Array)
    {
        notify(randomMember(msg));
    } else if (typeof msg === "string")
    {
        var es = document.getElementsByClassName("pio-dialog");
        if (es.length === 0)
        {
            es = window.parent.document.getElementsByClassName("pio-dialog");
        }
        for (var i = 0, max = es.length; i < max; i++) {
            var e = es[i];
            e.innerHTML = msg;
            if (!activeNotified)
            {
               // window.localStorage.setItem('activeNotified','true');
               activeNotified = true;
                e.classList.toggle('active');
                
            }
        }
        //console.log(window.localStorage.getItem('activeNotified'));
        var x = setInterval(function () {
            e.classList.toggle('active');
            //window.localStorage.setItem('activeNotified','false');
            activeNotified = false;
            clearInterval(x);
        }, 3000);
    } else
    {
        alert('there is an error in the girl');
        console.log(typeof msg);
    }


}
/**
 * This function Pio girl to speak up each time when the mouse of the user hovers over an element contains title.
 * @returns {undefined}
 */
function listenMouseTitle()
{
    window.addEventListener('mouseover', function (event) {
        var title = event.target.title;
        if (title === "" || title === null || typeof title === "undefined")
        {

        } else
        {
            notify(title);
        }
    });
}

