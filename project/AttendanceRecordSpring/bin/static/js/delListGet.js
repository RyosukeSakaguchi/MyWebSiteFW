/**
 *
 */
var count;
function BoxChecked(check) {
	for (count = 0; count < document.form1["delListId"].length; count++) {
		document.form1["delListId"][count].checked = check; // チェックボックスをON/OFFにする
	}
}
