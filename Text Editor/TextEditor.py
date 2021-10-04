from tkinter import *
from tkinter import filedialog
from tkinter import font

root = Tk()
root.title('Basic Text Editor')
root.geometry("1200x660")

# Set variable for open file name
global open_status_name
open_status_name = False

global selected
selected = False

# Create new file function
def new_file():
	# Delete pervious text
	my_text.delete(1.0, END)
	# Update status bars
	root.title('New File - Basic Text Editor')
	status_bar.config(text = "New File        ")

# Create open file function
def open_file():
	# Delete pervious text
	my_text.delete(1.0, END)

	# Grab filename
	text_file = filedialog.askopenfilename(title = "Open File", filetypes=(("Text Files", "*.txt"), ("HTML Files", "*.html"), ("Python Files", "*.py"), ("All Files", "*.*")))
	name = text_file

	# Update status bars
	status_bar.config(text = f'{name}        ')
	root.title(f'{name} - Basic Text Editor')

	# Open file
	text_file = open(text_file, 'r')
	stuff = text_file.read()

	# Add file to text box
	my_text.insert(END, stuff)

	# Close opened file
	text_file.close()

# Create save as file function
def save_as_file():
	text_file = filedialog.asksaveasfilename(defaultextension = ".*", title = "Save File", filetypes=(("Text Files", "*.txt"), ("HTML Files", "*.html"), ("Python Files", "*.py"), ("All Files", "*.*")))
	if text_file:
		# Update status bars
		name = text_file
		status_bar.config(text = f'Saved: {name}        ')
		root.title(f'{name} - Basic Text Editor')

		# Save file
		text_file = open(text_file, 'w')
		text_file.write(my_text.get(1.0, END))

		# Close the file
		text_file.close()


# Save File
def save_file():
	global open_status_name
	if open_status_name:
		# Save the file
		text_file = open(open_status_name, 'w')
		text_file.write(my_text.get(1.0, END))
		# Close the file
		text_file.close()
		# Put status update or popup code
		status_bar.config(text=f'Saved: {open_status_name}        ')
		name = open_status_name
		name = name.replace("C:/gui/", "")
		root.title(f'{name} - TextPad!')
	else:
		save_as_file()

# Cut Text
def cut_text(e):
	global selected
	# Check to see if keyboard shortcut used
	if e:
		selected = root.clipboard_get()
	else:
		if my_text.selection_get():
			# Grab selected text from text box
			selected = my_text.selection_get()
			# Delete Selected Text from text box
			my_text.delete("sel.first", "sel.last")
			# Clear the clipboard then append
			root.clipboard_clear()
			root.clipboard_append(selected)

# Copy Text
def copy_text(e):
	global selected
	# check to see if we used keyboard shortcuts
	if e:
		selected = root.clipboard_get()

	if my_text.selection_get():
		# Grab selected text from text box
		selected = my_text.selection_get()
		# Clear the clipboard then append
		root.clipboard_clear()
		root.clipboard_append(selected)

# Paste Text
def paste_text(e):
	global selected
	#Check to see if keyboard shortcut used
	if e:
		selected = root.clipboard_get()
	else:
		if selected:
			position = my_text.index(INSERT)
			my_text.insert(position, selected)


# Create main frame
my_frame = Frame(root)
my_frame.pack(pady = 5)

# Create scrollbar for the text box
text_scroll = Scrollbar(my_frame)
text_scroll.pack(side = RIGHT, fill = Y)

# Create text box
my_text = Text(my_frame, width = 97, height = 25, font = ("Arial", 16), selectbackground = "yellow", selectforeground = "black", undo = True, yscrollcommand = text_scroll.set)
my_text.pack()

# Configure scrollbar
text_scroll.config(command = my_text.yview)

# Create menu
my_menu = Menu(root)
root.config(menu = my_menu)

# Add file menu
file_menu = Menu(my_menu, tearoff=False)
my_menu.add_cascade(label="File", menu = file_menu)
file_menu.add_command(label = "New", command = new_file)
file_menu.add_command(label = "Open", command = open_file)
file_menu.add_command(label = "Save" , command = save_file)
file_menu.add_command(label = "Save As", command = save_as_file)
file_menu.add_separator()
file_menu.add_command(label = "Exit", command = root.destroy)

# Add edit menu
edit_menu = Menu(my_menu, tearoff=False)
my_menu.add_cascade(label="Edit", menu = edit_menu)
edit_menu.add_command(label="Cut", command=lambda: cut_text(False), accelerator="(Ctrl+x)")
edit_menu.add_command(label="Copy", command=lambda: copy_text(False), accelerator="(Ctrl+c)")
edit_menu.add_command(label="Paste             ", command=lambda: paste_text(False), accelerator="(Ctrl+v)")
edit_menu.add_separator()
edit_menu.add_command(label="Undo", command=my_text.edit_undo, accelerator="(Ctrl+z)")
edit_menu.add_command(label="Redo", command=my_text.edit_redo, accelerator="(Ctrl+y)")

# Add status bar to bottom of app
status_bar = Label(root, text = 'Ready        ', anchor = E)
status_bar.pack(fill = X, side = BOTTOM, ipady  = 5)

# Edit Bindings
root.bind('<Control-Key-x>', cut_text)
root.bind('<Control-Key-c>', copy_text)
root.bind('<Control-Key-v>', paste_text)


root.mainloop()
