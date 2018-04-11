<!DOCTYPE html>
<html lang="en">
<head>
<title>ACE in Action</title>
<style type="text/css" media="screen">
#editor {
	position: absolute;
	top: 0;
	right: 0;
	bottom: 0;
	left: 0;
}
</style>


	
</head>
<body>
<div id="editor-container" style="width:100px; height:100px">
	<div id="editor" style="width:700px; height:100px; position:relative">public static void main(String argrs[]){
	System.out.println("Hello there!");
}
	</div>
	<script src="../../js/ace/src-min-noconflict/ace.js" type="text/javascript"
		charset="utf-8"></script>
	<script>
		var editor = ace.edit("editor");
		editor.setTheme("ace/theme-tomorrow");
		editor.session.setMode("ace/mode/java");
	</script>
	<script src="../../js/ace/main-ace.js"></script>	
</div>
	<div id="btn">
		<button type="submit" onClick="submitCode()">Submit</button>
	</div>
</body>
</html>