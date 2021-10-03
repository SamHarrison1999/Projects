using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class Wait : MonoBehaviour
{

    void Start()
    {
        StartCoroutine(WaitCo());
    }

    IEnumerator WaitCo()
    {
        yield return new WaitForSeconds(3f);
        SceneManager.LoadScene(1);
    }

}
