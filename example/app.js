const win = Ti.UI.createWindow();
const mediaControl = require('ti.mediacontrol');

const btn = Ti.UI.createButton({title: "create player"});
btn.addEventListener("click", function() {
	mediaControl.showNotification({
		title: "-",
		text: "-"
	});
})
win.add(btn);
win.open();

const audioPlayer = Ti.Media.createAudioPlayer({
	url: 'http://streams.90s90s.de/hiphop/mp3-192/streams.90s90s.de/',
	allowBackground: true
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
