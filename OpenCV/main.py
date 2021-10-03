# !/usr/bin/env python3
import cv2, sys, math
import numpy as np

"""

This program finds out when the defender will intercept the ball.
Run this program via command line with the command python main.py <no of images> left-%3.3d.png right-%3.3d.png

Example output: 

frame          x_ball         y_ball         z_ball         x_defender     y_defender     z_defender     
1              440.0          227.0          455.0          191.0          242.0          358.0          
2              451.0          237.0          400.0          227.0          256.0          323.0          
3              465.0          249.0          371.0          271.0          273.0          278.0          
[The Images are 480 x 640 x 3 pixels.]
The defender will hit the ball in frame 6
"""

"""
Find the circumcircle of an object using the built in open cv method.
This funciton finds the minimal enclosing circle of a 2D point set.
I found information on how to use the built in minimum enclosing circle method
at https://docs.opencv.org/4.5.0/dd/d49/tutorial_py_contour_features.html
"""

def getMinimumEnclosingCircle (contours, i):
    (x,y),radius = cv2.minEnclosingCircle(contours[i])
    x_position = int(x)
    y_position = int(y)
    radius = int(radius)
    return x_position, y_position, radius

"""
I used the distance formular to calcuate the distance between two points in a xyz-space.
The distance formula states that if P1 = (x1,y1,z1) and P2 = (x2,y2,z2) the distance between
p1 and p2 = ((x2 - x1)^2 + (y2 - y1)^2 + (z2 - z1)^2)^1/2. I found information on how
to calculate the distance between two points in a 3D space at 
https://www.engineeringtoolbox.com/distance-relationship-between-two-points-d_1854.html
"""

def getDistance(i):
    return(math.sqrt(int(pow(x_ball_position[i] - x_defender_position[i],2))+int(pow(y_ball_position[i] - y_defender_position[i],2))+int(pow(z_ball_position[i] - z_defender_position[i],2)))) 

"""
Contours are a curves that join all the continuous points along the boundary
that have the same colour. I used the built in open cv method find contours 
in my program to detect objects then I sorted them according to their size by
using a comination of the Python sorted function and the cv2.contourArea method
I found information on how to use open cv's built in find contours method at 
https://docs.opencv.org/4.5.0/d4/d73/tutorial_py_contours_begin.html
"""

def getContours(mask):
    _, contours, _ = cv2.findContours(mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    contours = sorted(contours, key=cv2.contourArea, reverse=False)
    return contours

"""
I made sepearate lists for the cooridnates for the ball, 
player and the distances between them.
"""

x_ball_position = list()
y_ball_position = list()
z_ball_position = list()
x_defender_position = list()
y_defender_position = list()
z_defender_position = list()
distance = list()
x_ball_distance = list()
y_ball_distance = list()
z_ball_distance = list()
x_defender_distance = list()
y_defender_distance = list()
z_defender_distance = list()
ball_diameter = 150 # ball diamter in mm

"""
Read the number of frames from the command line. Sets the value
of first argument you entered in the command line to the frames variable
"""

frames = int(sys.argv[1])

# Prints equally distanced table header
 
print ("{: <15}{: <15}{: <15}{: <15}{: <15}{: <15}{: <15}". format ("frame", "x_ball", "y_ball", "z_ball", "x_defender", "y_defender", "z_defender"))

for frame in range(1, frames+1):

    # Form the left and right file names

    left_fn = sys.argv[2] % frame
    right_fn = sys.argv[3] % frame

    # Read in the images

    left = cv2.imread(left_fn)
    right = cv2.imread(right_fn)

    # Displays images to the user for a set amount of time before closing all the windows

    cv2.imshow("left-00" + str(frame), left)
    cv2.waitKey(600)
    cv2.imshow("right-00" + str(frame), right)
    cv2.waitKey(600)

    ny, nx, nc = left.shape

    """
    I used open cv's build in method to change the colour space
    to convert my image from BGR to HSV. I found informaiton on 
    changing colourspaces at 
    https://opencv24-python-tutorials.readthedocs.io/en/stable/py_tutorials/py_imgproc/py_colorspaces/py_colorspaces.html  
    """

    hsv_left = cv2.cvtColor(left, cv2.COLOR_BGR2HSV)
    hsv_right = cv2.cvtColor(right, cv2.COLOR_BGR2HSV)

    """
    I used numpys array method to create arrays for upper and lower bounds
    for colour ranges so that I would only detect objects that are within a certain colour range. 
    The specific colours I looked for was blue and white as the defender is represented 
    by a blue cirle and the football is represented by a white circle in the images I was
    given.
    """
    
    lower_blue = np.array([104,0,0])
    upper_blue = np.array([255,152,255])
    lower_white = np.array([200,200,220])
    upper_white = np.array([255,255,255])

    """
    For better accuracy before finding contours I applied thresholding using only cv2 
    threshold method.I found information on thresholding images at 
    https://docs.opencv.org/4.5.0/d4/d73/tutorial_py_contours_begin.html
    I displayed the thresholded images for verification purposes.
    """

    imgray_L = cv2.cvtColor(left, cv2.COLOR_BGR2GRAY)
    blur_l = cv2.medianBlur (imgray_L,15)
    circles = cv2.HoughCircles(blur_l, cv2.HOUGH_GRADIENT, 1.2, 100)
    ret_L, thresh_L = cv2.threshold(blur_l, 255, 255, 255)
    _, contours, hierarchy = cv2.findContours(thresh_L, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    cv2.imshow("left-00" + str(frame) +  "-greyscale" , thresh_L)
    cv2.waitKey(600)

    imgray_R = cv2.cvtColor(right, cv2.COLOR_BGR2GRAY)
    blur_r = cv2.medianBlur (imgray_R,15)
    ret_R, thresh_R = cv2.threshold(blur_r, 255, 255, 255)
    _, contours, hierarchy = cv2.findContours(thresh_R, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    cv2.imshow("right-00" + str(frame) + "-greyscale", thresh_R)
    cv2.waitKey(600)


    """
    I used open cvs in range method to make it so I only detected blue and white objects in 
    each frame
    """

    defender_mask_L = cv2.inRange(left, lower_blue, upper_blue)
    defender_mask_R = cv2.inRange(right, lower_blue, upper_blue)
    ball_mask_L = cv2.inRange(left, lower_white, upper_white)
    ball_mask_R = cv2.inRange(right, lower_white, upper_white)

    """
    I applied the mask I created to the images provided using open cvs bitwise_or method,
    then I displayed the masked images. 
    """

    mask_L = cv2.bitwise_or(defender_mask_L,ball_mask_L)
    targetL = cv2.bitwise_and(left, left, mask=mask_L)
    cv2.imshow("left-00" + str(frame) + "-mask", targetL)
    cv2.waitKey(600)
    mask_R = cv2.bitwise_or(defender_mask_R,ball_mask_R)
    targetR = cv2.bitwise_and(right, right, mask=mask_R)
    cv2.imshow("right-00" + str(frame) + "-mask", targetR)
    cv2.waitKey(600)


    # Calls the method that finds the contours using the masks I created

    defender_Contours_L = getContours(defender_mask_L)
    defender_Contours_R = getContours(defender_mask_R)
    ball_Contours_L = getContours(ball_mask_L)
    ball_Contours_R = getContours(ball_mask_R)

    """ 
    Calls the minimum enclosing circle method to return the x, y coordinates
    and radius for every object in the frame
    """

    defender_position_X_L, defender_position_Y_L, defender_radius_L = getMinimumEnclosingCircle(defender_Contours_L, 0)
    cv2.circle(left, (defender_position_X_L, defender_position_Y_L), defender_radius_L, (0, 0, 0), 4)

    ball_position_X_L, ball_position_Y_L, ball_radius_L = getMinimumEnclosingCircle(ball_Contours_L, -1)
    cv2.circle(left, (ball_position_X_L, ball_position_Y_L), ball_radius_L, (0, 0, 0), 4)

    defender_position_X_R, defender_position_Y_R, defender_radius_R = getMinimumEnclosingCircle(defender_Contours_R, 0)
    cv2.circle(right, (defender_position_X_R, defender_position_Y_R), defender_radius_R, (0, 0, 0), 4)

    ball_position_X_R, ball_position_Y_R, ball_radius_R = getMinimumEnclosingCircle(ball_Contours_R, -1)
    cv2.circle(right, (ball_position_X_R, ball_position_Y_R), ball_radius_R, (0, 0, 0), 4)

    """
    Cacluates the z position for the ball and defender in each frame.
    """

    ball_position_Z = (50 * (20*10)) / ((ball_position_X_L - nx / 2) - (ball_position_X_R - nx / 2))
    defender_position_Z = (50 * (20*10)) / ((defender_position_X_L - nx / 2) - (defender_position_X_R - nx / 2))

    cv2.destroyAllWindows()

    # Prints table body in the same same format as table header.

    print ("{: <15}{: <15}{: <15}{: <15}{: <15}{: <15}{: <15}". format (frame, str(round(ball_position_X_L, 2)), str(round(ball_position_Y_L, 2)), str(abs(round(ball_position_Z, 2))), str(round(defender_position_X_L, 2)), str(round(defender_position_Y_L, 2)), str(abs(round(defender_position_Z, 2)))))
   
    # Adds center coordinates of objects to seperate lists

    x_ball_position.append(round(ball_position_X_L, 2))
    y_ball_position.append(round(ball_position_Y_L, 2))
    z_ball_position.append(round(ball_position_Z, 2))
    x_defender_position.append(round(defender_position_X_L, 2))
    y_defender_position.append(round(defender_position_Y_L, 2))
    z_defender_position.append(round(defender_position_Z, 2))

"""
Works out the distance an object moves betweem 2 frames
"""

for i in range(0, frames):
    distance.append(getDistance(i))
    if i >= 1:
        x_ball_distance.append(x_ball_position[i] - x_ball_position[i - 1])
        y_ball_distance.append(y_ball_position[i] - y_ball_position[i - 1])
        z_ball_distance.append(z_ball_position[i] - z_ball_position[i - 1])
        x_defender_distance.append(x_defender_position[i] - x_defender_position[i - 1])
        y_defender_distance.append(y_defender_position[i] - y_defender_position[i - 1])
        z_defender_distance.append(z_defender_position[i] - z_defender_position[i - 1])

while True:
    x_ball_distance.append(np.median(x_ball_distance))
    y_ball_distance.append(np.median(y_ball_distance))
    z_ball_distance.append(np.median(z_ball_distance))
    x_defender_distance.append(np.median(x_defender_distance))
    y_defender_distance.append(np.median(y_defender_distance))
    z_defender_distance.append(np.median(z_defender_distance))
    # Predicts position of ball and defender for next frame
    x_ball_position.append(x_ball_position[-1] + x_ball_distance[-1])
    y_ball_position.append(y_ball_position[-1] + y_ball_distance[-1])
    z_ball_position.append(z_ball_position[-1] + z_ball_distance[-1])
    x_defender_position.append(x_defender_position[-1] + x_defender_distance[-1])
    y_defender_position.append(y_defender_position[-1] + y_defender_distance[-1])
    z_defender_position.append(z_defender_position[-1] + z_defender_distance[-1])

    """
    works out if the defender will intercept the ball
    """

    distance.append(getDistance(-1))
    if not (min(distance) == distance[-1]):
        interception = False
        break
    elif distance[-1] < ball_diameter:
        interception = True
        break

# prints out the size in pixels of the images
print("[The Images are %d x %d x %d pixels.]" % (ny, nx, nc))

if interception == True:
    print('The defender will hit the ball in frame ' + str(len(distance)))
else:
    print("The defender will not intercept the ball")

