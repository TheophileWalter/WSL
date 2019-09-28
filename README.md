<h1>Walter Stack Language</h1>
The Walter Stack Language is a LIFO stack based language.<br />
All the data and codes are stored in this stack.<br />
The language functions and keywords allows you to compute operations on that stack.<br />
<br />
<h3>Basics</h3>
A script is a succession of instructions separated with "whites charactes" (space, tabulation, line-break)<br />
A comment starts with <code>**</code> and finish with a line-break<br />
You can put some code in the stack (eg. for conditional execution) by surrounding it with parentheses (<i>cf</i>. type <code>^Group</code>)<br />
<h3>Data types</h3>
There is five data type in WSL
<ul>
  <li><code><b>^Number</b></code><br />
    Represents a number (integer or float)<br />
    <u>Example</u>: <code>0 1 42 5.2 -4 -5.78</code><br />
  </li><br />
  <li><code><b>^Boolean</b></code><br />
    Represents a boolean (0 or 1)<br />
    This is a subset of <code>^Number</code><br />
    <u>Example</u>: <code>0 1</code><br />
    <code>true</code> and <code>false</code> are defined in <i>lib.wsl</i><br />
  </li><br />
  <li><code><b>^String</b></code><br />
    Represents a string<br />
    <u>Example</u>: <code>"Hello world!"</code><br />
  </li><br />
  <li><code><b>^Group</b></code><br />
    Represents a group of instructions<br />
    To put a new group in the stack, write code inside parentheses<br />
    <u>Example</u>: <code>("Hello" print)</code><br />
  </li><br />
  <li><code><b>^Keyword</b></code><br />
    Represents a function or a keyword name<br />
    A keyword cannot being put in the stack, it will be called instead<br />
    <u>Example</u>: <code>keyword</code><br />
  </li><br />
  In this reference, the type <code>^a'</code> represents any type<br />
  When a function can take multiple different types, the notation is <code>^Type1|^Type2|...|^TypeN</code>, for example <code>^String|^Number</code> for a string and a number<br />
</ul>
<h3>Language keywords</h3>
<ul>
  <li>
    <b>call</b><br />
    <code>^String -> call</code><br />
    Call a keyword, a function or a defined group from a string<br />
    <u>Example</u>: <code>"Hello" "print" call ** Will print "Hello"</code><br />
  </li><br />
  <li>
    <b>def</b><br />
    <code>^String -> ^a' -> def</code><br />
    Define a value to be called later<br />
    <u>Example</u>: <code>"pi" 3.1415926535 def ** When you'll call the keyword "pi", 3.1415926535 will be put in the stack</code></br />
    If the value to define is a group, then when you'll call it, the code inside the group will be called<br />
    <u>Example</u>: <code>"group" ("Hello" print) def ** You can now call the code with the keyword "group"</code><br />
    If the definition is in a group, the name will be prefixed with the parent group name<br />
    <u>Example</u>: <code>"g1" ("g2" ("Hello" print) def) def ** You must call g2 with the name g1.g2</code><br />
    <i>Note</i>: To save a group, put it in a group<br />
    <u>Example</u>: <code>"g" ((123 456)) def</code><br />
  </li><br />
  <li>
    <b>exec</b><br />
    <code>^Group -> exec</code><br />
    Execute the given group<br />
    <u>Example</u>: <code>("Hello" print) exec ** Will print "Hello"</code><br />
  </li><br />
  <li>
    <b>global</b><br />
    <code>^String -> ^a' -> def</code><br />
    Same as <code>def</code> but if the definition is in a group, the name will not be prefixed<br />
  </li><br />
  <li>
    <b>group_prefix</b><br />
    <code>group_prefix -> ^String</code><br />
    Put the prefix of the current group in the stack<br />
    The prefix is the name of the currently executed group with a final dot<br />
    If this keyword is called outside a group, it will return an empty string<br />
    <u>Example</u>: <code>"g1" ("g2" (group_prefix print) def) static g1.g2 ** Will print "g1.g2."</code><br />
  </li><br />
  <li>
    <b>if</b><br />
    <code>^Group -> ^Group -> ^Boolean -> if</code><br />
    Execute one of the two groups based on the boolean value<br />
    <u>Examples</u>:<br />
    <div class="indent">
      <code>("123") ("456") 1 if print ** Will print "123"</code><br />
      <code>("123") ("456") 0 if print ** Will print "456"</code><br />
    </div>
  </li><br />
  <li>
    <b>parent_prefix</b><br />
    <code>parent_prefix -> ^String</code><br />
    Put the prefix of the parent group in the stack<br />
    The prefix is the name of the group that called the currently executed group with a final dot<br />
    If this keyword is called outside a group, or if the parent is not a group, it will return an empty string<br />
    <u>Example</u>: <code>"a" (parent_prefix print) def "foo" ("bar" (a) def) static foo.bar ** Will print "foo.bar."</code><br />
  </li><br />
  <li>
    <b>repeat</b><br />
    <code>^Group -> ^Number -> repeat</code><br />
    Repeat the code in the group a certain number of times<br />
    <u>Example</u>: <code>("Hello" print) 5 repeat ** Will print "Hello" five times</code><br />
  </li><br />
  <li>
    <b>source</b><br />
    <code>^String -> source</code><br />
    Execute a wsl script<br />
    The stack and the defined values will be shared between the two scripts<br />
    If the syntax checking or execution fails, the process will stop<br />
    <u>Example</u>: <code>"file.wsl" source ** Will execute the file "file.wsl"</code><br />
    The directory containing the default WSL libraries can is defined as <code>@lib</code><br />
    <u>Example</u>: <code>"@lib/lib.wsl" source ** Will execute the default WSL library</code><br />
  </li><br />
  <li>
    <b>static</b><br />
    <code>^String -> ^a' -> static</code><br />
    Define a static group to be called later<br />
    A static value is a group that contains only sub-groups declarations, it will be executed one time right after the declaration to evaluate the sub-groups<br />
    <u>Example</u>: <code>"group" ("sub1" ("hello") def "sub2" ("world") def) static ** You can now call the codes with the keywords "group.sub1" and "group.sub2"</code><br />
    If the definition is in a group, the name will be prefixed with the parent group name (as for <code>def</code>)<br />
    <i>Note</i>: If you use it with a value instead of a group, the value will be put in the stack<br />
  </li><br />
</ul>
<h3>Built-in functions</h3>
<ul>
  <li>
    <b>add</b><br />
    <code>^Number -> ^Number -> add -> ^Number</code><br />
    Add two numbers and put the result in the stack<br />
    <u>Example</u>: <code>4 7 add ** Will put 11 in the stack</code><br />
  </li><br />
  <li>
    <b>concat</b><br />
    <code>^String -> ^String -> concat -> ^String</code><br />
    Concatenate two strings and put the result in the stack<br />
    <u>Example</u>: <code>"Hello " "world!" concat ** Will put "Hello world!" in the stack</code><br />
  </li><br />
  <li>
    <b>div</b><br />
    <code>^Number -> ^Number -> div -> ^Number</code><br />
    Divide two numbers and put the result in the stack<br />
    <u>Example</u>: <code>5 2 div ** Will put 2.5 in the stack</code><br />
  </li><br />
  <li>
    <b>dup</b><br />
    <code>^Number -> dup</code><br />
    Duplicate a given number of elements in the stack<br />
    <u>Example</u>: <code>1 2 3 4 5 3 dup ** Will copy 3, 4 and 5 so the stack will be "1 2 3 4 5 3 4 5"</code><br />
  </li><br />
  <li>
    <b>equal</b><br />
    <code>^a' -> ^a' -> equal -> ^Boolean</code><br />
    Check if the two elements in the stack are equals and put 1 in the stack if they are, 0 if not<br />
    The two elements to compare must be the same type<br />
    <u>Examples</u>:
      <div class="indent">
        <code>"hello" "hello" equal ** Will put 1 in the stack</code><br />
        <code>"hello" "world" equal ** Will put 0 in the stack</code><br />
      </div>
  </li><br />
  <li>
    <b>exit</b><br />
    <code>^Number -> exit</code><br />
    Exit the script with the given code<br />
    The code will be casted to an integer if a float is given
    <u>Example</u>: <code>0 exit ** Exit with code 0</code><br />
  </li><br />
  <li>
    <b>max</b><br />
    <code>^Number -> ^Number -> max -> ^Number</code><br />
    Compare two numbers and put the greatest in the stack<br />
    <u>Example</u>: <code>5 42 max ** Will put 42 in the stack</code><br />
  </li><br />
  <li>
    <b>mb</b><br />
    <code>^a' -> ^Number -> mb</code><br />
    Move an element back in the stack<br />
    <u>Example</u>: <code>"a" "b" "c" "d" 2 mb ** Will move "d" two positions back, the stack will be "a" "d" "b" "c"</code><br />
  </li><br />
  <li>
    <b>min</b><br />
    <code>^Number -> ^Number -> min -> ^Number</code><br />
    Compare two numbers and put the smallest in the stack<br />
    <u>Example</u>: <code>5 42 min ** Will put 5 in the stack</code><br />
  </li><br />
  <li>
    <b>mod</b><br />
    <code>^Number -> ^Number -> mod -> ^Number</code><br />
    Compute the modulo of two numbers<br />
    <u>Example</u>: <code>10 3 mod ** Will put 1 in the stack</code><br />
  </li><br />
  <li>
    <b>mul</b><br />
    <code>^Number -> ^Number -> mul -> ^Number</code><br />
    Multiply two numbers and put the result in the stack<br />
    <u>Example</u>: <code>6 7 mul ** Will put 42 in the stack</code><br />
  </li><br />
  <li>
    <b>number</b><br />
    <code>^String|^Number -> number -> ^Number</code><br />
    Convert a number or a string to a number and put it in the stack<br />
    <u>Example</u>: <code>"5.3" number ** Will put 5.3 in the stack (with type ^Number)</code><br />
  </li><br />
  <li>
    <b>pop</b><br />
    <code>^a' -> pop</code><br />
    Remove the element on top of the stack<br />
    <u>Example</u>: <code>42 pop ** Will remove 42 from the stack</code><br />
  </li><br />
  <li>
    <b>print</b><br />
    <code>^String -> print</code><br />
    Print a string to standard output<br />
    Only a string can be printed<br />
    <u>Example</u>: <code>"Hello" print ** Will print "Hello"</code><br />
  </li><br />
  <li>
    <b>random</b><br />
    <code>random -> ^Number</code><br />
    Put an float random number in the stack (between 0 and 1 included)<br />
    <u>Example</u>: <code>"Random float: " random uconcat println</code><br />
  </li><br />
  <li>
    <b>size</b><br />
    <code>size -> ^Number</code><br />
    Put the size of the stack in the stack<br />
    <u>Example</u>: <code>"a" 5 4 (print) size ** Will put 4 on top of the stack</code><br />
  </li><br />
  <li>
    <b>sqrt</b><br />
    <code>^Number -> sqrt -> ^Number</code><br />
    Compute the square root of a number and put it in the stack<br />
    <u>Example</u>: <code>9 sqrt ** Will put 3 in the stack</code><br />
  </li><br />
  <li>
    <b>string</b><br />
    <code>^a' -> string -> ^String</code><br />
    Convert any value to a string<br />
    <u>Examples</u>:
      <div class="indent">
        <code>456 string ** Will put "456" in the stack</code><br />
        <code>("Hello" print) string ** Will put "TGroup&lt;...&gt;" in the stack (this is the default string representation for a group)</code><br />
      </div>
  </li><br />
  <li>
    <b>sub</b><br />
    <code>^Number -> ^Number -> sub -> ^Number</code><br />
    Substract two numbers and put the result in the stack<br />
    <u>Example</u>: <code>5 7 sub ** Will put -2 in the stack</code><br />
  </li><br />
</ul>
<h3>Examples</h3>
<i>Note that the examples require the default language library "lib.wsl"</i><br /><br />
<b>Example #1:</b> The Fibonacci sequence<br />
<pre>** Define a group called "fibonacci"
"fibonacci" (
	** Create a code that will be executed the required number of times
	(
		** Copy the two previous values
		2 dup
		** Sum them
		sum
	)
	** Invert the code group and the repetition times to be able
	** to call "repeat" with parameters in the correct order
	exch
	** Now the stack contains a group and a number,
	** so we can call "repeat" to execute the code
	** the desired number of time
	repeat
) def

** Compute the 7 values of Fibonacci sequence after 0 and 1
0 1 7 fibonacci

** Print the stack
stack.pretty_print</pre><br />
<b>Example #2:</b> The Fibonacci sequence (uncommented version)<br />
<pre>"fibonacci" ((2 dup sum) exch repeat) def

0 1 7 fibonacci stack.pretty_print</pre><br />
<b>Example #3:</b> Recursive call<br />
<pre>10 "rec" (copy uprintln 1 sub copy (rec) exch () exch 0 ifgt) def rec</pre><br />
<b>Example #4:</b> Usage of <code>this</code><br />
The group <code>this</code> is defined in the standard library, it can be used to call a group defined in the current group without knowing the name of this current group.<br />
It can be used for recursive call with a non-stack paradigm<br />
<br />
Here is a recursive call example in javascript :<br />
<pre>function rec(n, max) {
	if (n < max) {
		console.log(n);
		rec(n+1, max);
	}
}
rec(0, 10);</pre><br />
<br />
And here is the equivalent code in WSL with <code>this</code><br />
<pre>"rec" (
	
	** Get the given parameters from the stack (in reversed order)
	"max" exch def
	"n" exch def
	
	** The code of the function
	
	** If group
	(
		** We call the "n" group defined above
		"n" this uprintln
		"n" this 1 add "max" this rec
	)
	** Else group
	()
	** Condition on arguments
	"n" this "max" this lf if
  
) def

** Call it with the parameters in the stack
0 10 rec</pre><br />
<br />
This code works well because at each recursive call, the current group name is modified. So the <code>n</code> group is called <code>rec.n</code> at first call, <code>rec.rec.n</code> at the second, <i>ect</i>.<br />
So every group created in each recursive call has an unique name and can be identified with <code>this</code><br />
<b>Warning</b>: If you use <code>this</code> in groups called with others functions (eg: <code>iflt</code>) it won't work because the group called will be <code>iflt.n</code>.<br />
To avoir these problems, use the stack as much as possible and use <code>this</code> as a last resort and only if you understand prefectly how it works.<br />
