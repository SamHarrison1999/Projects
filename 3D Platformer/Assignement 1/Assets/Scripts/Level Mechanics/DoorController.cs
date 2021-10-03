using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class DoorController : MonoBehaviour
{
    //public bool shouldOpen;

    public Transform theDoor, openRotation;

    public float openSpeed;

    private Quaternion startRotation;

    public ButtonController theButton;

    // Start is called before the first frame update
    void Start()
    {
        startRotation = transform.rotation;
    }

    // Update is called once per frame
    void Update()
    {
        if (theButton.isPressed)
        {
            theDoor.rotation = Quaternion.Slerp(theDoor.rotation, openRotation.rotation, openSpeed * Time.deltaTime);
        }else
        {
            theDoor.rotation = Quaternion.Slerp(theDoor.rotation, startRotation, openSpeed * Time.deltaTime);
        }
    }
}
