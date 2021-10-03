using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class LSLevelEntry : MonoBehaviour
{
    public string levelName, levelToCheck, displayName;

    private bool canLoadLevel, levelUnlocked, levelLoading;

    public GameObject mapPointActive, mapPointInactive;

    // Start is called before the first frame update
    void Start()
    {
        if (PlayerPrefs.GetInt(levelToCheck + "_unlocked") == 1 || levelToCheck == "")
        {
            mapPointActive.SetActive(true);
            mapPointInactive.SetActive(false);
            levelUnlocked = true;
        } else
        {
            mapPointActive.SetActive(false);
            mapPointInactive.SetActive(true);
            levelUnlocked = false;
        }

        if (PlayerPrefs.GetString("CurrentLevel") == levelName)
        {
            PlayerController.instance.transform.position = transform.position;
            LSResetPosition.instance.respawnPosition = transform.position;
        }
    }

    // Update is called once per frame
    void Update()
    {
        if(Input.GetButtonDown("Jump") && canLoadLevel && levelUnlocked && !levelLoading)
        {
            StartCoroutine(LevelLoadCo());
            levelLoading = true;
        }
    }

    private void OnTriggerEnter(Collider other)
    {
        if (other.tag == "Player")
        {
            canLoadLevel = true;

            LSUIManager.instance.levelNamePanel.SetActive(true);
            LSUIManager.instance.levelNameText.text = displayName;

            if(PlayerPrefs.HasKey(levelName + "_coins"))
            {
                LSUIManager.instance.coinsText.text = PlayerPrefs.GetInt(levelName + "_coins").ToString();
            }
            else
            {
                LSUIManager.instance.coinsText.text = "???";
            }
        }
    }

    private void OnTriggerExit(Collider other)
    {
        if (other.tag == "Player")
        {
            canLoadLevel = false;

            LSUIManager.instance.levelNamePanel.SetActive(false);
        }
    }

    public IEnumerator LevelLoadCo()
    {
        PlayerController.instance.stopMove = true;
        UIManager.instance.fadeToBlack = true;

        yield return new WaitForSeconds(2f);

        SceneManager.LoadScene(levelName);
        PlayerPrefs.SetString("CurrentLevel", levelName);
    }
}
