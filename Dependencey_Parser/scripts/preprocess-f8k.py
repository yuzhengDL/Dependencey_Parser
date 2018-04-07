"""
Preprocessing script for Stanford Sentiment Treebank data.

"""

import os
import glob

def main():
    print('=' * 80)
    print('Preprocessing Flickr8K')
    print('=' * 80)

    base_dir = os.path.dirname(os.path.dirname(os.path.realpath(__file__)))
    data_dir = os.path.join(base_dir, 'data')
    lib_dir = os.path.join(base_dir, 'lib')
    f8k_dir = os.path.join(data_dir, 'Flickr8k')

    # produce train/dev/test splits
    sent_paths = glob.glob(os.path.join(f8k_dir, '*/*.txt'))

    # produce dependency parses
    classpath = ':'.join([
        lib_dir,
        os.path.join(lib_dir, 'stanford-parser/stanford-parser.jar'),
        os.path.join(lib_dir, 'stanford-parser/stanford-parser-3.9.1-models.jar')])
    for filepath in sent_paths:
        dependency_parse(filepath, cp=classpath, tokenize=False)


def dependency_parse(filepath, cp='', tokenize=True):
    print('\nDependency parsing ' + filepath)
    dirpath = os.path.dirname(filepath)
    filepre = os.path.splitext(os.path.basename(filepath))[0]
    tokpath = os.path.join(dirpath, filepre + '.toks')
    parentpath = os.path.join(dirpath, 'dparents.txt')
    relpath =  os.path.join(dirpath, 'rels.txt')
    tokenize_flag = '-tokenize - ' if tokenize else ''
    cmd = ('java -cp %s DependencyParse -tokpath %s -parentpath %s -relpath %s %s < %s'
        % (cp, tokpath, parentpath, relpath, tokenize_flag, filepath))
    os.system(cmd)

if __name__ == '__main__':
    main()

