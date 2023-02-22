var win = Ti.UI.createWindow();
var audioPlayer = Ti.Media.createAudioPlayer({
	url: 'http://icecast.unitedradio.it/Radio105.mp3',
  allowBackground: true
});
win.open();

var mediaControl = require('ti.mediacontrol');
mediaControl.showNotification({
	title: "my music",
	text: "just some text"
});

mediaControl.addEventListener("currentStatus", function(e) {
	console.log(e.status, mediaControl.PLAYING);
	if (e.status == mediaControl.PAUSED) {
		audioPlayer.pause();
	} else if (e.status == mediaControl.PLAYING) {
		audioPlayer.start();
	}
})
