var win = Ti.UI.createWindow();
var audioPlayer = Ti.Media.createAudioPlayer({
	url: 'http://streams.90s90s.de/hiphop/mp3-192/streams.90s90s.de/',
	allowBackground: true
});
win.open();

var mediaControl = require('ti.mediacontrol');
mediaControl.showNotification({
	title: "-",
	text: "-"
});

mediaControl.addEventListener("changeStatus", function(e) {
	if (e.status == mediaControl.PAUSE) {
		audioPlayer.pause();
		mediaControl.title = ""
		mediaControl.text = ""
	} else if (e.status == mediaControl.PLAY) {
		audioPlayer.start();
		mediaControl.updateInfo({
			title: "music playing",
			text: "some radio station"
		})
	}
})
