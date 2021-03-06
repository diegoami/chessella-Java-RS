







function DWRUtil() { }












DWRUtil.onReturn = function(event, action)
{
if (!event)
{
event = window.event;
}

if (event && event.keyCode && event.keyCode == 13)
{
action();
}
};









DWRUtil.selectRange = function(ele, start, end)
{
var orig = ele;
ele = $(ele);
if (ele == null)
{
alert("selectRange() can't find an element with id: " + orig + ".");
return;
}

if (ele.setSelectionRange)
{
ele.setSelectionRange(start, end);
}
else if (ele.createTextRange)
{
var range = ele.createTextRange();
range.moveStart("character", start);
range.moveEnd("character", end - ele.value.length);
range.select();
}

ele.focus();
};

















function $()
{
var elements = new Array();

for (var i = 0; i < arguments.length; i++)
{
var element = arguments[i];
if (typeof element == 'string')
{
if (document.getElementById)
{
element = document.getElementById(element);
}
else if (document.all)
{
element = document.all[element];
}
}

if (arguments.length == 1)
{
return element;
}

elements.push(element);
}

return elements;
}










DWRUtil.toDescriptiveString = function(data, level, depth)
{
var reply = "";
var i = 0;
var value;
var obj;

if (level == null)
{
level = 0;
}

if (depth == null)
{
depth = 0;
}

if (data == null)
{
return "null";
}

if (DWRUtil._isArray(data))
{
if (level != 0)
{
reply += "[\n";
}
else
{
reply = "[";
}

for (i = 0; i < data.length; i++)
{
try
{
obj = data[i];

if (obj == null || typeof obj == "function")
{
continue;
}
else if (typeof obj == "object")
{
if (level > 0)
{
value = DWRUtil.toDescriptiveString(obj, level - 1, depth + 1);
}
else
{
value = DWRUtil._detailedTypeOf(obj);
}
}
else
{
value = "" + obj;
value = value.replace(/\/n/g, "\\n");
value = value.replace(/\/t/g, "\\t");
}
}
catch (ex)
{
value = "" + ex;
}

if (level != 0)
{
reply += DWRUtil._indent(level, depth + 2) + value + ", \n";
}
else
{
if (value.length > 13)
{
value = value.substring(0, 10) + "...";
}

reply += value + ", ";

if (i > 5)
{
reply += "...";
break;
}
}
}

if (level != 0)
{
reply += DWRUtil._indent(level, depth) + "]";
}
else
{
reply += "]";
}

return reply;
}

if (typeof data == "string" || typeof data == "number" || DWRUtil._isDate(data))
{
return data.toString();
}

if (typeof data == "object")
{
var typename = DWRUtil._detailedTypeOf(data);
if (typename != "Object")
{
reply = typename + " ";
}

if (level != 0)
{
reply += "{\n";
}
else
{
reply = "{";
}

var isHtml = DWRUtil._isHTMLElement(data);

for (var prop in data)
{
if (isHtml)
{
if (prop.toUpperCase() == prop || prop == "title" ||
prop == "lang" || prop == "dir" || prop == "className" ||
prop == "form" || prop == "name" || prop == "prefix" ||
prop == "namespaceURI" || prop == "nodeType" ||
prop == "firstChild" || prop == "lastChild" ||
prop.match(/^offset/))
{

continue;
}
}

value = "";

try
{
obj = data[prop];

if (obj == null || typeof obj == "function")
{
continue;
}
else if (typeof obj == "object")
{
if (level > 0)
{
value = "\n";
value += DWRUtil._indent(level, depth + 2);
value = DWRUtil.toDescriptiveString(obj, level - 1, depth + 1);
}
else
{
value = DWRUtil._detailedTypeOf(obj);
}
}
else
{
value = "" + obj;
value = value.replace(/\/n/g, "\\n");
value = value.replace(/\/t/g, "\\t");
}
}
catch (ex)
{
value = "" + ex;
}

if (level == 0 && value.length > 13)
{
value = value.substring(0, 10) + "...";
}

var propStr = prop;
if (propStr.length > 30)
{
propStr = propStr.substring(0, 27) + "...";
}

if (level != 0)
{
reply += DWRUtil._indent(level, depth + 1);
}

reply += prop;
reply += ":";
reply += value;
reply += ", ";

if (level != 0)
{
reply += "\n";
}

i++;
if (level == 0 && i > 5)
{
reply += "...";
break;
}
}

reply += DWRUtil._indent(level, depth);
reply += "}";

return reply;
}

return data.toString();
};





DWRUtil._indent = function(level, depth)
{
var reply = "";
if (level != 0)
{
for (var j = 0; j < depth; j++)
{
reply += "\u00A0\u00A0";
}
reply += " ";
}
return reply;
};





DWRUtil.useLoadingMessage = function()
{
var disabledZone = document.createElement('div');
disabledZone.setAttribute('id', 'disabledZone');
disabledZone.style.position = "absolute";
disabledZone.style.zIndex = "1000";
disabledZone.style.left = "0px";
disabledZone.style.top = "0px";
disabledZone.style.width = "100%";
disabledZone.style.height = "100%";
document.body.appendChild(disabledZone);

var messageZone = document.createElement('div');
messageZone.setAttribute('id', 'messageZone');
messageZone.style.position = "absolute";
messageZone.style.top = "0px";
messageZone.style.right = "0px";
messageZone.style.background = "red";
messageZone.style.color = "white";
messageZone.style.fontFamily = "Arial,Helvetica,sans-serif";
messageZone.style.padding = "4px";
disabledZone.appendChild(messageZone);

var text = document.createTextNode('Loading');
messageZone.appendChild(text);

$('disabledZone').style.visibility = 'hidden';

DWREngine.setPreHook(function() { $('disabledZone').style.visibility = 'visible'; });
DWREngine.setPostHook(function() { $('disabledZone').style.visibility = 'hidden'; });
};











DWRUtil.setValue = function(ele, val)
{
if (val == null)
{
val = "";
}

var orig = ele;
ele = $(ele);
if (ele == null)
{
alert("setValue() can't find an element with id: " + orig + ".");
return;
}

if (DWRUtil._isHTMLElement(ele, "select"))
{


var found  = false;
var i;

for (i = 0; i < ele.options.length; i++)
{
if (ele.options[i].value == val)
{
ele.options[i].selected = true;
found = true;
}
else
{
ele.options[i].selected = false;
}
}


if (found)
{
return;
}

for (i = 0; i < ele.options.length; i++)
{
if (ele.options[i].text == val)
{
ele.options[i].selected = true;
break;
}
}

return;
}

if (DWRUtil._isHTMLElement(ele, "input"))
{
switch (ele.type)
{
case "checkbox":
case "check-box":
case "radio":
ele.checked = (val == true);
return;

default:
ele.value = val;
return;
}
}

if (DWRUtil._isHTMLElement(ele, "textarea"))
{
ele.value = val;
return;
}



if (val.nodeType)
{
if (val.nodeType == 9  )
{
val = val.documentElement;
}

val = DWRUtil._importNode(ele.ownerDocument, val, true);
ele.appendChild(val);
return;
}


ele.innerHTML = val;
};








DWRUtil.getValue = function(ele)
{
var orig = ele;
ele = $(ele);
if (ele == null)
{
alert("getValue() can't find an element with id: " + orig + ".");
return "";
}

if (DWRUtil._isHTMLElement(ele, "select"))
{


var sel = ele.selectedIndex;
if (sel != -1)
{
var reply = ele.options[sel].value;
if (reply == null || reply == "")
{
reply = ele.options[sel].text;
}

return reply;
}
else
{
return "";
}
}

if (DWRUtil._isHTMLElement(ele, "input"))
{
switch (ele.type)
{
case "checkbox":
case "check-box":
case "radio":
return ele.checked;

default:
return ele.value;
}
}

if (DWRUtil._isHTMLElement(ele, "textarea"))
{
return ele.value;
}

return ele.innerHTML;
};







DWRUtil.getText = function(ele)
{
var orig = ele;
ele = $(ele);
if (ele == null)
{
alert("getText() can't find an element with id: " + orig + ".");
return "";
}

if (!DWRUtil._isHTMLElement(ele, "select"))
{
alert("getText() can only be used with select elements. Attempt to use: " + DWRUtil._detailedTypeOf(ele) + " from  id: " + orig + ".");
return "";
}



var sel = ele.selectedIndex;
if (sel != -1)
{
return ele.options[sel].text;
}
else
{
return "";
}
};







DWRUtil.setValues = function(map)
{
for (var property in map)
{
var ele = $(property);
if (ele != null)
{
var value = map[property];
DWRUtil.setValue(property, value);
}
}
};







DWRUtil.getValues = function(map)
{
for (var property in map)
{
var ele = $(property);
if (ele != null)
{
map[property] = DWRUtil.getValue(property);
}
}
};










































DWRUtil.addOptions = function(ele, data, valuerev, textprop)
{
var orig = ele;
ele = $(ele);
if (ele == null)
{
alert("addOptions() can't find an element with id: " + orig + ".");
return;
}

var useOptions = DWRUtil._isHTMLElement(ele, "select");
var useLi = DWRUtil._isHTMLElement(ele, ["ul", "ol"]);

if (!useOptions && !useLi)
{
alert("fillList() can only be used with select elements. Attempt to use: " + DWRUtil._detailedTypeOf(ele));
return;
}


if (data == null)
{
return;
}

var text;
var value;
var opt;

if (DWRUtil._isArray(data))
{

for (var i = 0; i < data.length; i++)
{
if (useOptions)
{
if (valuerev != null)
{
if (textprop != null)
{
text = data[i][textprop];
value = data[i][valuerev];
}
else
{
value = data[i][valuerev];
text = value;
}
}
else
{
if (textprop != null)
{
text = data[i][textprop];
value = text;
}
else
{
text = "" + data[i];
value = text;
}
}

opt = new Option(text, value);
ele.options[ele.options.length] = opt;
}
else
{
li = document.createElement("li");
li.innerHTML = "" + data[i];
ele.appendChild(li);
}
}
}
else
{
for (var prop in data)
{
if (!useOptions)
{
alert("DWRUtil.addOptions can only create select lists from objects.");
return;
}

if (valuerev)
{
text = prop;
value = data[prop];
}
else
{
text = data[prop];
value = prop;
}

opt = new Option(text, value);
ele.options[ele.options.length] = opt;
}
}
};






DWRUtil.removeAllOptions = function(ele)
{
var orig = ele;
ele = $(ele);
if (ele == null)
{
alert("removeAllOptions() can't find an element with id: " + orig + ".");
return;
}

var useOptions = DWRUtil._isHTMLElement(ele, "select");
var useLi = DWRUtil._isHTMLElement(ele, ["ul", "ol"]);

if (!useOptions && !useLi)
{
alert("removeAllOptions() can only be used with select, ol and ul elements. Attempt to use: " + DWRUtil._detailedTypeOf(ele));
return;
}

if (useOptions)
{

ele.options.length = 0;
}
else
{
while (ele.childNodes.length > 0)
{
ele.removeChild(ele.firstChild);
}
}
};





























DWRUtil.addRows = function(ele, data, cellFuncs)
{
var orig = ele;
ele = $(ele);
if (ele == null)
{
alert("addRows() can't find an element with id: " + orig + ".");
return;
}

if (!DWRUtil._isHTMLElement(ele, ["table", "tbody", "thead", "tfoot"]))
{
alert("addRows() can only be used with table, tbody, thead and tfoot elements. Attempt to use: " + DWRUtil._detailedTypeOf(ele));
return;
}



if (navigator.product && navigator.product == "Gecko")
{
setTimeout(function() { DWRUtil._addRowsInner(ele, data, cellFuncs); }, 0);
}
else
{
DWRUtil._addRowsInner(ele, data, cellFuncs);
}
};






DWRUtil._addRowsInner = function(ele, data, cellFuncs)
{
var frag = document.createDocumentFragment();

if (DWRUtil._isArray(data))
{

for (var i = 0; i < data.length; i++)
{
DWRUtil._addRowInner(frag, data[i], cellFuncs);
}
}
else if (typeof data == "object")
{
for (var row in data)
{
DWRUtil._addRowInner(frag, row, cellFuncs);
}
}

ele.appendChild(frag);
};





DWRUtil._addRowInner = function(frag, row, cellFuncs)
{
var tr = document.createElement("tr");

for (var j = 0; j < cellFuncs.length; j++)
{
var func = cellFuncs[j];
var td;

if (typeof func == "string")
{
td = document.createElement("td");
var text = document.createTextNode(func);
td.appendChild(text);
tr.appendChild(td);
}
else
{
var reply = func(row);

if (DWRUtil._isHTMLElement(reply, "td"))
{
td = reply;
}
else if (DWRUtil._isHTMLElement(reply))
{
td = document.createElement("td");
td.appendChild(reply);
}
else
{
td = document.createElement("td");
td.innerHTML = reply;


}

tr.appendChild(td);
}
}

frag.appendChild(tr);
};








DWRUtil.removeAllRows = function(ele)
{
var orig = ele;
ele = $(ele);
if (ele == null)
{
alert("removeAllRows() can't find an element with id: " + orig + ".");
return;
}

if (!DWRUtil._isHTMLElement(ele, ["table", "tbody", "thead", "tfoot"]))
{
alert("removeAllRows() can only be used with table, tbody, thead and tfoot elements. Attempt to use: " + DWRUtil._detailedTypeOf(ele));
return;
}

while (ele.childNodes.length > 0)
{
ele.removeChild(ele.firstChild);
}
};











DWRUtil._agent = navigator.userAgent.toLowerCase();




DWRUtil._isIE = ((DWRUtil._agent.indexOf("msie") != -1) && (DWRUtil._agent.indexOf("opera") == -1));









DWRUtil._isHTMLElement = function(ele, nodeName)
{
if (ele == null || typeof ele != "object" || ele.nodeName == null)
{
return false;
}

if (nodeName != null)
{
var test = ele.nodeName.toLowerCase();

if (typeof nodeName == "string")
{
return test == nodeName.toLowerCase();
}

if (DWRUtil._isArray(nodeName))
{
var match = false;
for (var i = 0; i < nodeName.length && !match; i++)
{
if (test == nodeName[i].toLowerCase())
{
match =  true;
}
}

return match;
}

alert("DWRUtil._isHTMLElement was passed test node name that is neither a string or array of strings");
return false;
}

return true;
};






DWRUtil._detailedTypeOf = function(x)
{
var reply = typeof x;

if (reply == "object")
{
reply = Object.prototype.toString.apply(x);
reply = reply.substring(8, reply.length-1);
}

return reply;
};










DWRUtil._isArray = function(data)
{
return (data && data.join) ? true : false;
};










DWRUtil._isDate = function(data)
{
return (data && data.toUTCString) ? true : false;
};






DWRUtil._importNode = function(doc, importedNode, deep)
{
var newNode;

if (importedNode.nodeType == 1  )
{
newNode = doc.createElement(importedNode.nodeName);

for (var i = 0; i < importedNode.attributes.length; i++)
{
var attr = importedNode.attributes[i];
if (attr.nodeValue != null && attr.nodeValue != '')
{
newNode.setAttribute(attr.name, attr.nodeValue);
}
}

if (typeof importedNode.style != "undefined")
{
newNode.style.cssText = importedNode.style.cssText;
}
}
else if (importedNode.nodeType == 3  )
{
newNode = doc.createTextNode(importedNode.nodeValue);
}

if (deep && importedNode.hasChildNodes())
{
for (i = 0; i < importedNode.childNodes.length; i++)
{
newNode.appendChild(DWRUtil._importNode(doc, importedNode.childNodes[i], true));
}
}

return newNode;
}
if (typeof document.importNode != "function")
{
document.importNode = function(importedNode, deep)
{
DWRUtil._importNode(this, importedNode, deep);
};
}











if (!DWRUtil.isHTMLElement)
{
DWRUtil.isHTMLElement = function(ele, nodeName)
{
DWRUtil._deprecated("DWRUtil.isHTMLElement");

if (nodeName == null)
{


return ele != null &&
typeof ele == "object" &&
ele.nodeName != null;
}
else
{
return ele != null &&
typeof ele == "object" &&
ele.nodeName != null &&
ele.nodeName.toLowerCase() == nodeName.toLowerCase();
}
};
}






if (!DWRUtil.detailedTypeOf)
{
DWRUtil.detailedTypeOf = function(x)
{
DWRUtil._deprecated("DWRUtil.detailedTypeOf");

var reply = typeof x;

if (reply == "object")
{
reply = Object.prototype.toString.apply(x);
reply = reply.substring(8, reply.length-1);
}

return reply;
};
}










if (!DWRUtil.isArray)
{
DWRUtil.isArray = function(data)
{
DWRUtil._deprecated("DWRUtil.isArray", "(array.join != null)");
return (data && data.join) ? true : false;
};
}










if (!DWRUtil.isDate)
{
DWRUtil.isDate = function(data)
{
return (data && data.toUTCString) ? true : false;
};
}







if (!DWRUtil.isHTMLInputElement)
{
DWRUtil.isHTMLInputElement = function(ele)
{
DWRUtil._deprecated("DWRUtil.isHTMLInputElement");
return DWRUtil.isHTMLElement(ele, "input");
};
}







if (!DWRUtil.isHTMLTextAreaElement)
{
DWRUtil.isHTMLTextAreaElement = function(ele)
{
DWRUtil._deprecated("DWRUtil.isHTMLTextAreaElement");
return DWRUtil.isHTMLElement(ele, "textarea");
};
}







if (!DWRUtil.isHTMLSelectElement)
{
DWRUtil.isHTMLSelectElement = function(ele)
{
DWRUtil._deprecated("DWRUtil.isHTMLSelectElement");
return DWRUtil.isHTMLElement(ele, "select");
};
}






if (!DWRUtil.getElementById)
{
DWRUtil.getElementById = function(id)
{
DWRUtil._deprecated("DWRUtil.getElementById", "$");

if (document.getElementById)
{
return document.getElementById(id);
}
else if (document.all)
{
return document.all[id];
}

return null;
};
}








if (!DWRUtil.setEnabled)
{
DWRUtil.setEnabled = function(ele, state)
{
DWRUtil._deprecated("DWRUtil.setEnabled");

var orig = ele;
ele = $(ele);
if (ele == null)
{
alert("setEnabled() can't find an element with id: " + orig + ".");
return;
}






ele.disabled = !state;
ele.readonly = !state;
if (DWRUtil._isIE)
{
if (state)
{
ele.style.backgroundColor = "White";
}
else
{

ele.style.backgroundColor = "Scrollbar";
}
}
};
}






if (!DWRUtil.showById)
{
DWRUtil.showById = function(ele)
{
DWRUtil._deprecated("DWRUtil.showById");

var orig = ele;
ele = $(ele);
if (ele == null)
{
alert("showById() can't find an element with id: " + orig + ".");
return;
}


ele.style.display = '';
};
}






if (!DWRUtil.hideById)
{
DWRUtil.hideById = function(ele)
{
DWRUtil._deprecated("DWRUtil.hideById");

var orig = ele;
ele = $(ele);
if (ele == null)
{
alert("hideById() can't find an element with id: " + orig + ".");
return;
}

ele.style.display = 'none';
};
}






if (!DWRUtil.toggleDisplay)
{
DWRUtil.toggleDisplay = function(ele)
{
DWRUtil._deprecated("DWRUtil.toggleDisplay");

var orig = ele;
ele = $(ele);
if (ele == null)
{
alert("toggleDisplay() can't find an element with id: " + orig + ".");
return;
}

if (ele.style.display == 'none')
{

ele.style.display = '';
}
else
{
ele.style.display = 'none';
}
};
}








if (!DWRUtil.alternateRowColors)
{
DWRUtil.alternateRowColors = function()
{
DWRUtil._deprecated("DWRUtil.alternateRowColors");

var tables = document.getElementsByTagName("table");
var rowCount = 0;

for (var i = 0; i < tables.length; i++)
{
var table = tables.item(i);
var rows = table.getElementsByTagName("tr");

for (var j = 0; j < rows.length; j++)
{
var row = rows.item(j);
if (row.className == "zebra")
{
if (rowCount % 2)
{
row.className = 'oddrow';
}
else
{
row.className = 'evenrow';
}

rowCount++;
}
}

rowCount = 0;
}
};
}






if (!DWRUtil.setCSSClass)
{
DWRUtil.setCSSClass = function(ele, cssclass)
{
DWRUtil._deprecated("DWRUtil.setCSSClass");

var orig = ele;
ele = $(ele);
if (ele == null)
{
alert("setCSSClass() can't find an element with id: " + orig + ".");
return;
}

ele.className = cssclass;
};
}






if (!DWRUtil.callOnLoad)
{
DWRUtil.callOnLoad = function(load)
{
DWRUtil._deprecated("DWRUtil.callOnLoad", "window.addEventListener or window.onload");

if (window.addEventListener)
{
window.addEventListener("load", load, false);
}
else if (window.attachEvent)
{
window.attachEvent("onload", load);
}
else
{
window.onload = load;
}
};
}






if (!DWRUtil.fillList)
{
DWRUtil.fillList = function(ele, data, valueprop, textprop)
{
DWRUtil._deprecated("DWRUtil.fillList", "DWRUtil.addOptions");
DWRUtil.removeAllOptions(ele);
DWRUtil.addOptions(ele, data, valueprop, textprop);
};
}





if (!DWRUtil.drawTable)
{
DWRUtil.drawTable = function(ele, data, cellFuncs)
{
DWRUtil._deprecated("DWRUtil.drawTable", "DWRUtil.addRows");
DWRUtil.addRows(ele, data, cellFuncs);
};
}








if (!DWRUtil.clearChildNodes)
{
DWRUtil.clearChildNodes = function(id)
{
DWRUtil._deprecated("DWRUtil.clearChildNodes", "DWRUtil.removeAllRows");

var orig = ele;
ele = $(ele);
if (ele == null)
{
alert("clearChildNodes() can't find an element with id: " + orig + ".");
return;
}

while (ele.childNodes.length > 0)
{
ele.removeChild(ele.firstChild);
}
};
}





DWRUtil._showDeprecated = true;






DWRUtil._deprecated = function(fname, altfunc)
{
if (DWRUtil._showDeprecated)
{
var warning;
var alternative;

if (fname == null)
{
warning = "You have used a deprecated function which could be removed in the future.";
alternative = "";
}
else
{
warning = "Utility functions like '" + fname + "' are deprecated and could be removed in the future.";

if (altfunc == null)
{
alternative = "\nSee the documentation for alternatives.";
}
else
{
alternative = "\nFor an alternative see: " + altfunc;
}
}

var further = "\nImport deprecated.js to get rid of this warning.\nDo you wish to ignore further deprecation warnings on this page?";

DWRUtil._showDeprecated = !confirm(warning + alternative + further);
}
};

