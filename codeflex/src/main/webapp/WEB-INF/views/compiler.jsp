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
	<!--  script src="../../js/ace/main-ace.js"></script> -->
	<script src="../../js/jquery-3.3.1.js"></script>
	<script src="../../js/ace/src-min-noconflict/ace.js"
		type="text/javascript" charset="utf-8"></script>
	<script>function submitCode(){
		$.post(
	            "http://localhost:8080/submission",
	            {
	            	language: 'JAVA',
            		code: btoa(ace.edit('editor').getValue())
	            }
	        );	
	}</script>

	<div id="editor-container" >
		<div id="editor"
			style="width: 700px; height: 600px; position: relative">public
			static void main(String args[]){ Syout.println("Hello there!"); }</div>
		<script>
			var editor = ace.edit("editor");
			editor.setTheme("ace/theme-tomorrow");
			editor.session.setMode("ace/mode/java");
		</script>


		$('.ace_text-layer').innerText


	</div>

	</div>
	<div id="btn">
		<button type="submit" onClick="submitCode()">Submit</button>
	</div>
</body>
</html>