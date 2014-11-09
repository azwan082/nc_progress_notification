var win = Ti.UI.createWindow({
    exitOnClose: true,
    title: 'Android Progress Notification',
    layout: 'vertical'
});

var startBtn = Ti.UI.createButton({
    title: 'Start progress'
});
var counter = 0;
var interval;
function runNotify() {
    counter += 10;
    notify(counter);
}
startBtn.addEventListener('click', function() {
    counter = 0;
    runNotify();
    interval = setInterval(runNotify, 1000);
});
win.add(startBtn);

var cancelBtn = Ti.UI.createButton({
    title: 'Cancel progress'
});
cancelBtn.addEventListener('click', function() {
    if (notif) {
        clearInterval(interval);
        notif.cancel();
        notif = null;
    }
});
win.add(cancelBtn);

var ProgressNotif = require('nc.progressnotification');
var notif;

function notify(progress) {
    if (!notif) {
        var intent = Ti.Android.currentActivity.intent;
        intent.setFlags(Ti.Android.FLAG_ACTIVITY_SINGLE_TOP);
        var pending = Ti.Android.createPendingIntent({
            intent: intent,
            updateCurrentIntent: true
        });
        notif = ProgressNotif.createNotification({
            id: 1234,
            number: 1,
            icon: Ti.App.Android.R.drawable.appicon,
            pendingIntent: pending,
            title: 'Filename.pdf'
        });
    }
    notif.text = 'Downloading...('+ progress +'%)';
    if (progress >= 100) {
        notif.text = 'Download complete';
        clearInterval(interval);
    }
    notif.notify(progress);
}

win.open();