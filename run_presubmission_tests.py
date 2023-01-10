import sys
import os
import getopt
import re
import subprocess
import shutil
import textwrap
from subprocess import call, PIPE, STDOUT
import glob
from os import listdir

# -------------------------------------------------------------
# here are the things that need to be changed between exercises 
# does 'subprocess.check_call(["hsmatch", userName ,"passwd"], stdout=tempf,stderr=tempf)' still check that userName exists in moodle systems?

EX_NAME = "ex6"

PROGRAM_FOLDER = os.path.dirname(os.path.realpath(__file__))
JUNIT_LOCATION = PROGRAM_FOLDER + "/junit4.jar"
# PROGRAM_FOLDER = "/cs/course/current/oop/Grading/ex6/"
# JUNIT_LOCATION = "/cs/course/current/oop/lib/junit4.jar"
# JUNIT4_PACKAGE="org.junit.runner.JUnitCore"
JUNIT_TEST_NAME = "Ex7TestRunner"
TEMP_FOLDER = PROGRAM_FOLDER + "/temp/"
SUBMISSIONS_FOLDER = PROGRAM_FOLDER + "/exercise_jar/"
OTHER_FILES_ALLOWED = True
TREE_PACKAGE = "oop/ex6/main/"
JAVA_FILES = [TREE_PACKAGE + "Sjavac.java"]
EX_IN_PAIRS = True
REQUIRED_FILES = JAVA_FILES + ["README"]
OPTIONAL_FILES = ["META-INF"]
NEEDED_FILES = [PROGRAM_FOLDER + "/unitTesters.jar", JUNIT_LOCATION]
wrapper = textwrap.TextWrapper(width=100, break_long_words=False, subsequent_indent="\t", initial_indent="\t",
                               replace_whitespace=False)

ex6StudentErrors = open("ex6_auto", "w")


# ---------------------------------------------------------------
# STRINGS MESSAGES

def ExtractJarFile(fileName, Dest):
    os.chdir(Dest)
    try:
        with open(os.devnull, 'w') as tempf:
            a = subprocess.check_call(["jar", "-xvf", fileName], stdout=tempf, stderr=tempf)
    except subprocess.CalledProcessError:
        print("Error extracting jar")
        ex6StudentErrors.write("\tunjar_error\n")
        return -1


def checkRequiredFiles(destFolder, otherFilesAllowed):
    error = False
    printDir(destFolder)
    for fileName in REQUIRED_FILES:
        javaFile = os.path.join(destFolder, fileName)
        if not os.path.isfile(javaFile):
            print("Error FILE: '" + fileName + "' is missing from archive")
            error = True
            ex6StudentErrors.write("\tmissing_files\n")

    if not otherFilesAllowed:
        for file in os.walk(destFolder):
            if file not in REQUIRED_FILES + OPTIONAL_FILES:
                print("Error FILE: '" + file + "' should not be in archive")
                error = True
                ex6StudentErrors.write("\textra_files\n")

    if error == True:
        return -1


def readReadme(destFolder):
    ReadmeUserName = []
    ReadmeFilePath = os.path.join(destFolder, "README")
    try:
        ReadmeFile = open(ReadmeFilePath, 'r')

        ReadmeUserName.append(ReadmeFile.readline().rstrip())
        if (EX_IN_PAIRS):
            ReadmeUserName.append(ReadmeFile.readline().strip())
            if ReadmeUserName[1] == '':
                ReadmeUserName = ReadmeUserName[:-1]

        for name in ReadmeUserName:
            if (len(name) < 2):
                print("Error README First line does not have ONLY a username " + name)
                raise Exception()
            if not re.match("^[a-z0-9_.]+$", name):
                print("Error username can only have [a-z0-9_.]")
                raise Exception()
    except IOError:
        print("Error cannot open README")
        raise Exception()

    return ReadmeUserName


def deleteFolderContent(folder):
    try:
        shutil.rmtree(folder)

    except Exception:
        pass
    os.makedirs(folder)


def copyNeededFiles(files, dest):
    for file in files:
        shutil.copy(file, dest)


def printDir(folder, fileType=None):
    print("printing files in " + folder)
    for file in os.listdir(folder):
        name, extension = os.path.splitext(file)
        if fileType is None:

            print("\t" + file)
        elif extension == fileType:
            print("\t" + file)


def compileCode(destFolder):
    os.chdir(destFolder)
    compileCommand = "javac -Xlint:rawtypes -Xlint:empty -Xlint:divzero -Xlint:deprecation -cp .:" + JUNIT_LOCATION + "  -encoding UTF-8 *.java " + TREE_PACKAGE + "*.java"
    print("compiling with \n\t" + compileCommand)
    process = subprocess.Popen(compileCommand, cwd=destFolder, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                               shell=True)
    out, err = process.communicate()
    error = err.decode("utf-8");

    if error != '':
        print("compile error :\n\t" + error)
        print('returned error')
        ex6StudentErrors.write("\tbad_submission\n")
        return -1

    if process.returncode != 0:
        print("error compiling")
        print("bad return code")
        ex6StudentErrors.write("\tbad_submission\n")
        return -1


def timeout_command(command, timeout, destFolder):
    """call shell-command and either return its output or kills it
    if it doesn't normally exit within timeout seconds and return None"""
    import subprocess, datetime, os, time, signal
    start = datetime.datetime.now()

    proc = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, cwd=destFolder)

    while proc.poll() is None:
        time.sleep(1)
        now = datetime.datetime.now()
        if (now - start).seconds > timeout:
            os.kill(proc.pid, signal.SIGKILL)
            os.waitpid(-1, os.WNOHANG)
            print("timeout")
            return None, None
    return proc.communicate()


def runJunitTesters(destFolder):
    command = ["java", "-cp", ".:" + JUNIT_LOCATION, JUNIT_TEST_NAME]
    timeout = 60
    (out, err) = timeout_command(command, timeout, destFolder)
    if err == None:
        print("Program ran too long During Testing -TimeOut")
        ex6StudentErrors.write("\ttest_timeout\n")
        return
    if len(err) > 1:
        print("tests error :\n\t" + (err).decode("utf-8"))
    message = (out).decode("utf-8")

    message = wrapper.fill(message)
    message = message.replace("runTests", "\nrunTests")

    import re
    p = re.compile(":\d{3}")
    for m in p.finditer(message):
        startingIndex = m.start()
        errorcode = "t" + message[startingIndex + 1:startingIndex + 4]
        ex6StudentErrors.write("\t" + errorcode + "\n")

    print("tests output :\n\t" + message)


def main(tempFolder, originalFolder, jarFile):
    try:
        destFolder = tempFolder
        deleteFolderContent(destFolder)

        copyNeededFiles([os.path.join(originalFolder, jarFile)], destFolder)
        if ExtractJarFile(jarFile, destFolder) == -1:
            return -1

        users = []
        try:
            users = readReadme(destFolder)
        except:
            ex6StudentErrors.write(jarFile + ":\n\tbad_readme\n")
            return -1

        ex6StudentErrors.write(users[0])
        if len(users) == 2:
            ex6StudentErrors.write(", " + users[1])
        ex6StudentErrors.write(":\n")

        # check if all the files expected exist
        if checkRequiredFiles(destFolder, OTHER_FILES_ALLOWED) == -1:
            return -1

        copyNeededFiles(NEEDED_FILES, destFolder)

        if ExtractJarFile(NEEDED_FILES[0], destFolder) == -1:
            return -1

        print("\n")
        if compileCode(destFolder) == -1:
            return -1

        print("\n")
        runJunitTesters(destFolder)
    except:
        ex6StudentErrors.write("\tbad_submission\n")
        return -1


if __name__ == "__main__":
    countSubmissionsChecked = 0
    tempFolder = TEMP_FOLDER
    originalFolder = SUBMISSIONS_FOLDER
    listOfsubmissions = listdir(originalFolder)
    for jarFile in listOfsubmissions:
        countSubmissionsChecked += 1
        print("Testing username " + jarFile)
        if main(tempFolder, originalFolder, jarFile) == -1:
            pass
        ex6StudentErrors.write("\n")
    ex6StudentErrors.close()
