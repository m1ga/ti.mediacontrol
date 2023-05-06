const win = Ti.UI.createWindow({layout:"vertical"});
const mediaControl = require('ti.mediacontrol');
const btn = Ti.UI.createButton({title: "create player"});
const btn_change = Ti.UI.createButton({title: "change"});
const btn_close = Ti.UI.createButton({title: "close"});

btn.addEventListener("click", function() {
	mediaControl.createPlayer({
		title: "-",
		text: "-",
		color: "#00f",
		showPrevious: true,
		showNext: true,
		backgroundImage: "/cover.jpg"
	});
})

btn_change.addEventListener("click", function() {
	mediaControl.backgroundImage = "/cover2.jpg"
	mediaControl.updateInfo({
		title: "music playing",
		text: "some radio station"
	})
})

btn_close.addEventListener("click", function() {
	mediaControl.close();
})

win.add([btn, btn_change, btn_close]);
win.open();

const audioPlayer = Ti.Media.createAudioPlayer({
	url: 'http://streams.90s90s.de/hiphop/mp3-192/streams.90s90s.de/',
	allowBackground: true
});

mediaControl.addEventListener("changeStatus", function(e) {

	if (e.status == mediaControl.PAUSE) {
		console.log("pause");
		audioPlayer.pause();
		mediaControl.title = ""
		mediaControl.text = ""
	} else if (e.status == mediaControl.PLAY) {
		console.log("play");
		audioPlayer.start();
		mediaControl.backgroundImage = "/cover2.jpg"
		mediaControl.updateInfo({
			title: "music playing",
			text: "some radio station"
		})
	} else if (e.status == mediaControl.NEXT) {
		console.log("next");
	} else if (e.status == mediaControl.PREVIOUS) {
		console.log("previous");
	}
})
