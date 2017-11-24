/**
 *
 */
var count;
function BoxChecked(check) {
	var array = document.form1["|delListId[${seq}]|"]
	console.log(array.length);
	for (count = 0; count < document.form1["|delListId[${seq}]|"].length; count++) {
		document.form1["|delListId[${seq}]|"][count].checked = check; // チェックボックスをON/OFFにする
	}
}
